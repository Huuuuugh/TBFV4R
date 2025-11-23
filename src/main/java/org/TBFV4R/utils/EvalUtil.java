package org.TBFV4R.utils;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;

public class EvalUtil {
    public static boolean evalBoolean(String assignment, String condition) {
        try (JShell jshell = JShell.create()) {
            jshell.eval(assignment);

            for (SnippetEvent e : jshell.eval(condition)) {
                if (e.value() != null) {
                    return Boolean.parseBoolean(e.value());
                }
            }
        }
        return false;
    }

    public static String replaceNumber(String input, int newNumber) {
        if (input == null) return null;
        return input.replaceAll("x\\s*=\\s*\\d+", "x=" + newNumber);
    }

}
