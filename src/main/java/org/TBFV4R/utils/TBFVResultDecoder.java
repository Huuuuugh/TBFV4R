package org.TBFV4R.utils;
import org.TBFV4R.TBFV.TBFVResult;

public class TBFVResultDecoder {
        public static String parse(TBFVResult result) {
            if (result == null) return "TBFVResult is null";

            StringBuilder sb = new StringBuilder();
            sb.append("Status: ").append(result.getStatus()).append("\n");
            sb.append("Counter Example: ").append(result.getCounterExample()).append("\n");
            sb.append("Path Constraint: ").append(result.getPathConstrain()).append("\n");

            switch (result.getStatus()) {
                case -3:
                    sb.append("Timeout error occurred during verification.\n");
                    break;
                case -2:
                    sb.append("Verification failed: Exception thrown by the program.\n");
                    break;
                case -1:
                    sb.append("Verification failed: Unexpected error.\n");
                    break;
                case 0:
                    sb.append("Partial success: Path verified successfully.\n");
                    break;
                case 1:
                    sb.append("Verificator error occurred.\n");
                    break;
                case 2:
                    sb.append("Verification failed: Output violated specification.\n");
                    break;
                case 3:
                    sb.append("Verification success!\n");
                    break;
                default:
                    sb.append("Unknown status.\n");
                    break;
            }

            return sb.toString();
        }


}
