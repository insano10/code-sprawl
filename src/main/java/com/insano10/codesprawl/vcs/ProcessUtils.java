package com.insano10.codesprawl.vcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessUtils
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
}
