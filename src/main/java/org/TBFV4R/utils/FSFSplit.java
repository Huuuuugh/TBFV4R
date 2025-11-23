package org.TBFV4R.utils;

import java.util.ArrayList;
import java.util.List;
public class FSFSplit {
    private static String removeNewlines(String input) {
        return input.replace("\n", "").replace("\r", "");
    }

    private static List<String> splitByDoublePipe(String input) {
        List<String> result = new ArrayList<>();
        for (String part : input.split("\\|\\|")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private static String[] splitByDoubleAmpersand(String rule) {
        String[] parts = rule.split("&&", 2);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }
    public static List<String[]> parseIFSFString(String input) {
        List<String[]> result = new ArrayList<>();
        String noNewlines = removeNewlines(input);
        List<String> rules = splitByDoublePipe(noNewlines);
        for (String r : rules) {
            result.add(splitByDoubleAmpersand(r));
        }
        return result;
    }
    public static List<String[]> parseFSFString(String input) {
        List<String[]> result = new ArrayList<>();
        String[] blocks = input.split("\\n\\s*\\n");

        for (String block : blocks) {
            String[] lines = block.strip().split("\\n");
            String t = null, d = null;

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("T")) {
                    t = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("D")) {
                    d = line.substring(line.indexOf(":") + 1).trim();
                }
            }

            if (t != null && d != null) {
                result.add(new String[]{t, d});
            }
        }

        return result;
    }

}
