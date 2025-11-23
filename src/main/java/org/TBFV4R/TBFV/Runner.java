package org.TBFV4R.TBFV;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import org.TBFV4R.tcg.ExecutionEnabler;
import org.TBFV4R.tcg.TestCaseAutoGenerator;
import org.TBFV4R.verification.SpecUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.TBFV4R.path.ExecutionPathPrinter.addPrintStmt;
import static org.TBFV4R.tcg.ExecutionEnabler.generateMainMdUnderExpr;
import static org.TBFV4R.tcg.ExecutionEnabler.insertMainMdInSSMP;
import static org.TBFV4R.tcg.TestCaseAutoGenerator.getDefaultValueOfType;

public class Runner {
    public enum TBFVValidationResultType {
        SUCCESS,
        REGENERATE,
        UNEXPECTED_ERROR,
        OVERTIME_ERROR
    }
    public static TBFVResult validate1Path(String ssmp, String mainMd, List<String> prePathConstrains, String T, String D) throws Exception {
        //
        String addedPrintProgram = addPrintStmt(ssmp);
        //
        String runnableProgram = insertMainMdInSSMP(addedPrintProgram, mainMd);
        System.out.println("runnableProgram: " + runnableProgram);
        //SpecUnit
        SpecUnit su = new SpecUnit(runnableProgram,T,D,prePathConstrains);
        TBFVResult r = Z3Solver.callZ3Solver(su);
        System.out.println("verification result: " + r);
        return r;
    }

    private static String validateExceptionPath(String ssmp, String t) {

        String mainMd = generateMainMdUnderExpr(t,null,ssmp);
        if(mainMd == null || mainMd.isEmpty() || mainMd.startsWith("ERROR")){
            System.out.println("[" + t + "], ");
            return "SUCCESS";
        }
        try {
            TBFVResult TBFVResult = validate1Path(ssmp, mainMd, null, t, "Exception");
            if(TBFVResult == null){
                System.out.println("，result");
                return "ERROR: No result returned during validation!";
            }
            if(TBFVResult.getStatus() == 0){
                return "SUCCESS";
            }
            if(TBFVResult.getStatus() == 1){
                return mainMd;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "FAILED";
    }
    public static String validateATAndD(String ssmp, String T, String D, int maxRoundsOf1CoupleOfTD, List<String> historyTestcases, List<TBFVResult> finalResultsOfTDS) throws Exception {
        int countOfPathValidated = 0;
        TBFVResult r = null;
        String currentTD = "T: " + T + "\t" + "D: " + D;
        List<String> prePathConstrains = new ArrayList<>();

        if(D.contains("Exception")){
            System.out.println("D  Exception");
            String vepr = validateExceptionPath(ssmp, T);
            if(vepr.contains("SUCCESS")){
                finalResultsOfTDS.add(new TBFVResult(3,"Exception",""));
                return "SUCCESS";
            }
            //LLM，TD
            return "Under T :" + T + "，" + "specifically when the variables are assigned like the main method showing: " + vepr + "No exception was thrown by the program. Think again and regenerate";
        }

        while(countOfPathValidated < maxRoundsOf1CoupleOfTD){
            //TD
            //main，

            String mainMd = generateMainMdUnderExpr(T,prePathConstrains,ssmp);
            historyTestcases.add(mainMd);
            if(mainMd == null || mainMd.isEmpty() || mainMd.startsWith("ERROR")){
                System.err.println("generate testcase failed！");
                return "ERROR: generate testcase under constrains " + T  + "failed!";
            }
            r = validate1Path(ssmp,mainMd,prePathConstrains,T,D);
            if(r == null){
                return "ERROR: unexpected error occurred during validation of " + currentTD + ", please check the log for details!";
            }
            if(r.getStatus() == 0){
                prePathConstrains.add(r.getPathConstrain());
                countOfPathValidated++;
                System.out.println(currentTD + "====>" + "The path [" + countOfPathValidated + "]verified successfully!");
            }
            else break;
        }
        if(r.getStatus() == -3){
            System.out.println("Timeout error occurred during validation of " + currentTD);
            return "OVERTIME ERROR!";
        }
        if(r.getStatus() == -2){
            System.out.println(currentTD + "\n" + "verification failed, there is an exception thrown by the program!");
            return "Unexpected exception thrown by the program under T: " + T + ", please regenerate the FSF according to this exception!" + r.getCounterExample();
        }
        if(r.getStatus() == -1){
            System.out.println(currentTD + "\n" + "verification failed, please check the log for details!");
            return "Some errors occurred while verifying" + currentTD +", "  + r.getCounterExample() + ", please regenerate the FSF!";
        }
        if(r.getStatus() == 0 && countOfPathValidated == maxRoundsOf1CoupleOfTD){
            System.out.println("The verified paths is over " + maxRoundsOf1CoupleOfTD + ", end of validation for " + currentTD);
            finalResultsOfTDS.add(r);
            return "PARTIALLY SUCCESS";
        }
        if(r.getStatus() == 3){
            System.out.println(currentTD + "====>" + "Verification success!");
            finalResultsOfTDS.add(r);
            return "SUCCESS";
        }
        if(r.getStatus() == 1){
            System.err.println(currentTD + "====>" + "Verificator error！");
            finalResultsOfTDS.add(r);
            return "ERROR: Verificator error！";
        }
        if(r.getStatus() == 2){
            System.out.println(currentTD + "\n" + "Verification failed!");
            System.out.println("the counterexample is ：\n" + r.getCounterExample());
            HashMap<String, String> map = TestCaseAutoGenerator.analyzeModelFromZ3Solver(r.getCounterExample(), ssmp);
            if(map.isEmpty()){
                System.out.println("No counterexample or something wrong happened while parsing counterexample!");
                return "ERROR: Something wrong happened while parsing counterexample!!";
            }
            StringBuilder counterExampleMsg = new StringBuilder();
            for(Map.Entry<String, String> entry : map.entrySet()){
                counterExampleMsg.append(entry.getKey()).append(": ").append(entry.getValue()).append("\t");
            }
            return "When the variables are assigned as "+counterExampleMsg+"，the output of the program violates " + currentTD + "，please regenerate the FSF according this counterexample！";
        }

        return "ERROR: unknown error occurred during validation of " + currentTD + ", please check the log for details!";
    }


    public static boolean isTotallyVerified(List<TBFVResult> finalResultsOfEveryCoupleOfTD){
        //，
        //，TD,
        boolean totallyVerified = true;
        for(TBFVResult res : finalResultsOfEveryCoupleOfTD){
            if(res.getStatus() == 1){
                System.out.println("validation task failed!");
                return false;
            }
            if(res.getStatus() == 0){
                totallyVerified = false;
            }
        }
        return totallyVerified;
    }

    public static TBFVResult validateWithTestCase(String ssmp, String currentT, String currentD, HashMap<String, String> testCaseMap) throws Exception {
        String runnableProgram;
        boolean hasMain = new JavaParser().parse(ssmp).getResult()
                .get()
                .findAll(MethodDeclaration.class)
                .stream()
                .anyMatch(md -> md.getNameAsString().equals("main"));

        if (hasMain) {
            runnableProgram = ssmp;
            if (testCaseMap != null && !testCaseMap.isEmpty()) {
                for (Map.Entry<String, String> entry : testCaseMap.entrySet()) {
                    String var = entry.getKey();
                    String value = entry.getValue();
                    runnableProgram = runnableProgram.replaceAll("Scanner\\s+.*\\.next.*\\(\\)", value);
                }
            }
        } else {
            StringBuilder mainBuilder = new StringBuilder();
            mainBuilder.append("public static void main(String[] args) {\n");

            if (testCaseMap != null) {
                MethodDeclaration md = ExecutionEnabler.getFirstStaticMethod(ssmp);
                List<Parameter> params = md.getParameters();
                for (Parameter param : params) {
                    String name = param.getNameAsString();
                    String type = param.getTypeAsString();
                    String value = testCaseMap.getOrDefault(name, getDefaultValueOfType(type));

                    if ("char".equals(type) && value.length() == 1) value = "'" + value + "'";
                    mainBuilder.append("    ").append(type).append(" ").append(name).append(" = ").append(value).append(";\n");
                }

                mainBuilder.append("    ");
                if (!md.getType().isVoidType()) mainBuilder.append(md.getType()).append(" result = ");
                mainBuilder.append(md.getNameAsString()).append("(");
                for (int i = 0; i < params.size(); i++) {
                    if (i > 0) mainBuilder.append(", ");
                    mainBuilder.append(params.get(i).getNameAsString());
                }
                mainBuilder.append(");\n");
                if (!md.getType().isVoidType()) mainBuilder.append("    System.out.println(result);\n");
            }

            mainBuilder.append("}\n");
            String mainMd = mainBuilder.toString();

            String addedPrintProgram = addPrintStmt(ssmp);
            runnableProgram = insertMainMdInSSMP(addedPrintProgram, mainMd);
        }
        System.out.println(runnableProgram);

        SpecUnit su = new SpecUnit(runnableProgram, currentT, currentD, new ArrayList<>());
        return Z3Solver.callZ3Solver(su);
    }


}
