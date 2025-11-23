import org.TBFV4R.utils.EvalUtil;
import org.junit.jupiter.api.Test;

public class TestEvalUtil {
    @Test
    void testEval(){
        System.out.println(EvalUtil.evalBoolean("var x=0;","x>-1"));;
    }
}
