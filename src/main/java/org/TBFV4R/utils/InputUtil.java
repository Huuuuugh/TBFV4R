package org.TBFV4R.utils;

import java.util.Optional;

public class InputUtil {
    public static Optional<Integer> processInput(String input) {
        if (input == null) return Optional.empty();
        String trimmed = input.trim();
        if (trimmed.isEmpty() ||
                trimmed.equalsIgnoreCase("accept") ||
                trimmed.equalsIgnoreCase("ok") ||
                trimmed.equalsIgnoreCase("yes") ||
                trimmed.equalsIgnoreCase("y") ||
                trimmed.equalsIgnoreCase("confirm") ||
                trimmed.equals("是") ||
                trimmed.equals("好") ||
                trimmed.equals("确认")) {
            return Optional.empty();
        }

        try {
            int value = Integer.parseInt(trimmed);
            return Optional.of(value);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
