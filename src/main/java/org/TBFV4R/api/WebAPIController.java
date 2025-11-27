package org.TBFV4R.api;

import org.TBFV4R.TBFV.Runner;
import org.TBFV4R.TBFV.TBFVResult;
import org.TBFV4R.llm.Model;
import org.TBFV4R.utils.EvalUtil;
import org.TBFV4R.utils.FSFSplit;
import org.TBFV4R.utils.TBFVResultDecoder;
import org.TBFV4R.utils.WebLoggingOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WebAPIController {

    private final Model model = new Model();
    private final Logger logger = LogManager.getLogger(WebAPIController.class);
    private final WebSocketConfig wsConfig;

    public WebAPIController(WebSocketConfig wsConfig) {
        this.wsConfig = wsConfig;
        PrintStream logOut = new PrintStream(new WebLoggingOutputStream(logger, false, wsConfig), true);
        System.setOut(logOut);
        PrintStream logErr = new PrintStream(new WebLoggingOutputStream(logger, true, wsConfig), true);
        System.setErr(logErr);
    }

    @PostMapping("/generateIFSF")
    public String generateIFSF(@RequestBody String code) {
        return model.code2IFSF(code);
    }

    @PostMapping("/generateFSF")
    public String generateFSF(@RequestBody String IFSF) {
        return model.IFSF2FSF(IFSF);
    }

    @PostMapping("/getFSFConditions")
    public List<Map<String, String>> getFSFConditions(@RequestBody String FSF) {
        List<String[]> fsfTwoPart = FSFSplit.parseFSFString(FSF);
        List<Map<String, String>> conditions = new ArrayList<>();
        for (String[] arr : fsfTwoPart) {
            Map<String, String> map = new HashMap<>();
            map.put("T", arr[0]);
            map.put("D", arr[1]);
            conditions.add(map);
        }
        return conditions;
    }

    @PostMapping("/generateTestCase")
    public String generateTestCase(@RequestParam String condition) {
        return model.generateTestCase(condition);
    }

    @PostMapping("/replaceTestCaseValue")
    public String replaceTestCaseValue(@RequestParam String testCase,
                                       @RequestParam int newValue,
                                       @RequestParam String condition) {
        String newTestCase = EvalUtil.replaceNumber(testCase, newValue);
        if(EvalUtil.evalBoolean("var " + newTestCase + ";", condition)){
            return newTestCase;
        } else {
            return "Invalid replacement, condition not satisfied.";
        }
    }

    @PostMapping("/simulateTestCase")
    public String simulateTestCase(@RequestParam String ssmp,
                                   @RequestParam String currentT,
                                   @RequestParam String currentD,
                                   @RequestBody Map<String, String> testCaseMap) {
        try {
            TBFVResult tbfvResult = Runner.validateWithTestCase(ssmp, currentT, currentD, testCaseMap);
            return TBFVResultDecoder.parse(tbfvResult);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
