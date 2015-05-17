package com.insano10.codesprawl.vcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtils
{
    public static String getSingleLineProcessOutput(Process process) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            return reader.readLine();
        }
    }
}
