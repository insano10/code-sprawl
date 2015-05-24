package com.insano10.codesprawl.vcs;

import com.insano10.codesprawl.source.ProjectFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VcsUtils
{
    public static String getSingleLineProcessOutput(Process process) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            return reader.readLine();
        }
    }

    public static List<String> getMultiLineProcessOutput(Process process) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public static void prependLinesIntoFile(List<String> lines, Path filePath) throws IOException
    {
        Path tmpPath = filePath.getParent().resolve("vcsUtilsPrepend.tmp");

        Files.write(tmpPath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(tmpPath, Files.readAllLines(filePath), StandardOpenOption.APPEND);
        Files.delete(filePath);
        Files.move(tmpPath, filePath);
    }
}
