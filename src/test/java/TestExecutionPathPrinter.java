import org.TBFV4R.path.TransWorker;
import org.TBFV4R.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.TBFV4R.path.ExecutionPathPrinter.addPrintStmt;

public class TestExecutionPathPrinter {
    @Test
    void testAddPrintStmt() throws IOException {
        String dir = "dataset/";
        String testFileName = "Abs_Original";
        String testFileNameJava = testFileName+".java";
        String testFilePath = dir + "/" + testFileNameJava;
        String ssmp = TransWorker.trans2SSMP(FileUtil.readLinesAsString(testFilePath,"\n"));
        System.out.println(addPrintStmt(ssmp));

    }
}
