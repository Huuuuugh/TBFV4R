package org.TBFV4R;

import org.TBFV4R.llm.Model;
import org.TBFV4R.utils.EvalUtil;
import org.TBFV4R.utils.FSFSplit;
import org.TBFV4R.utils.FileUtil;
import org.TBFV4R.utils.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        String code = FileUtil.readLinesAsString("dataset/Abs_Original.java","\n");
        System.out.println("Original Code & Description");
        System.out.println(code);
        System.out.println();

        String IFSF = model.code2IFSF(code);
        System.out.println("Informal FSF:");
        System.out.println(IFSF);
        System.out.println();

        String FSF = model.IFSF2FSF(IFSF);
        System.out.println("Formal FSF:");
        System.out.println(FSF);
        System.out.println();

        List<String[]> fsfTwoPart = FSFSplit.parseFSFString(IFSF);
        System.out.println("Select a test condition:");
        for (int i = 0; i < fsfTwoPart.size(); i++) {
            System.out.println("\t"+(i+1)+")"+fsfTwoPart.get(i)[0]+"\t(T"+(i+1)+")");
        }
        System.out.print("Enter index:");
        Scanner s = new Scanner(System.in);
        int line = s.nextInt();
        line-=1;
        String condition = fsfTwoPart.get(line)[0];
        String testCase = model.generateTestCase(condition);
        System.out.println("Proposed test case: "+testCase);
        boolean passTest = false;
        if(EvalUtil.evalBoolean("var "+testCase,condition)){
            System.out.println("Test Case:"+testCase+" satisfied condition " + condition);
            System.out.println("Press Enter or type one of {accept/ok/yes/y/confirm/是/好/确认} to accept.");
            System.out.println("Or input ONE integer to replace:");
            passTest=true;
        }else{
            System.out.println("Test Case:"+testCase+" can not satisfied condition " + condition + " please check it manually");
            System.out.println("input ONE integer to replace:");
        }
        do {
            Optional<Integer> input = InputUtil.processInput(s.nextLine());
            if (input.isPresent()) {
                int newValue = input.get();
                passTest=false;
                String newTestCase = EvalUtil.replaceNumber(testCase,newValue);
                if(EvalUtil.evalBoolean("var "+newTestCase,condition)){
                    passTest=true;
                    testCase = newTestCase;
                    System.out.println("The new test case is " + testCase);
                }else{
                    System.out.println("The integer does not satisfied condition " + condition);
                    System.out.println("input ONE integer to replace:");
                }
            }
        }while(!passTest);

        System.out.println("[Run] Simulate test case");


    }
}
