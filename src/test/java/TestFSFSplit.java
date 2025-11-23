import org.TBFV4R.utils.FSFSplit;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class TestFSFSplit {
    @Test
    void testFSFSplit(){
        List<String[]> fsfTwoPart = FSFSplit.parseFSFString("x is greater than 0 &&\n" +
                "  n becomes the least non-negative integer such that\n" +
                "  ((n)-1)*(n)/2<x && (1+(n))*(n)/2>=x ||\n" +
                "\n" +
                "  x is less than or equal to 0 &&\n" +
                "  n becomes 0");
        System.out.println("Select a test condition:");
        for (int i = 0; i < fsfTwoPart.size(); i++) {
            System.out.println("\t"+(i+1)+")"+fsfTwoPart.get(i)[0]+"\t(T"+(i+1)+")");
        }
        System.out.print("Enter index:");
    }
    @Test
    void getBase64(){
        String suJson = "{\n" +
                "  \"program\": \"public class Abs_Original {\\n\\n    public static int Abs(int num) {\\n        System.out.println(\\\"Function input int parameter num = \\\" + (num));\\n        if (num < 0) {\\n            System.out.println(\\\"Evaluating if condition: (num < 0) is evaluated as: \\\" + (num < 0));\\n            System.out.println(\\\"return_value = -num , current value of return_value : \\\" + (-num));\\n            return -num;\\n        } else {\\n            System.out.println(\\\"Evaluating if condition: !(num < 0) is evaluated as: \\\" + !(num < 0));\\n            System.out.println(\\\"return_value = num , current value of return_value : \\\" + (num));\\n            return num;\\n        }\\n    }\\n\\n    public static void main(String[] args) {\\n        int num = 1;\\n        int result = Abs(num);\\n        System.out.println(result);\\n    }\\n}\",\n" +
                "  \"preconditions\": [\"num == 1\"],\n" +
                "  \"T\": \"num >= 0\",\n" +
                "  \"D\": \"return_value == 1\",\n" +
                "  \"pre_constrains\": []\n" +
                "}\n";
        String encoded = Base64.getEncoder().encodeToString(suJson.getBytes(StandardCharsets.UTF_8));
        System.out.println(encoded);
        System.out.println(suJson);
    }
}
