package org.TBFV4R.api;

import org.TBFV4R.Main;
import org.TBFV4R.TBFV.Runner;
import org.TBFV4R.TBFV.TBFVResult;
import org.TBFV4R.llm.Model;
import org.TBFV4R.utils.EvalUtil;
import org.TBFV4R.utils.FSFSplit;
import org.TBFV4R.utils.TBFVResultDecoder;
import org.TBFV4R.utils.WebLoggingOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.util.*;

@SpringBootApplication
public class WebAPI {
    public static void main(String[] args) {
        SpringApplication.run(WebAPI.class, args);
    }
}
