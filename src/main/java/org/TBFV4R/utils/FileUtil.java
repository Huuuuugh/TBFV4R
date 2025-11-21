package org.TBFV4R.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {
    public static String readLinesAsString(String filePath, String delimiter) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        return String.join(delimiter, lines);
    }
}
