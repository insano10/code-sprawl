package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class GitVcsControl implements VcsControl
{
    private static final Logger LOGGER = Logger.getLogger(GitVcsControl.class);

    @Override
    public void generateVcsLog(Path vcsRootPath, Path vcsLogPath)
    {
        LOGGER.info("generating Git log at: " + vcsLogPath);

        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cd " + vcsRootPath + "; git log --name-only > " + vcsLogPath});
            process.waitFor();

        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.error("Failed to generate Git log from: " + vcsRootPath + " to: " + vcsLogPath);
        }
    }

    @Override
    public String getCurrentVcsRevision(Path vcsRootPath)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cd " + vcsRootPath + "; git log -1 | grep 'commit ' | cut -d' ' -f2"});
            process.waitFor();

            return ProcessUtils.getSingleLineProcessOutput(process);
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current Git revision", e);
        }
    }

    @Override
    public String getLatestVcsLogRevision(Path vcsLogPath)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "head -n1 " + vcsLogPath + " | cut -d' ' -f2"});
            process.waitFor();

            return ProcessUtils.getSingleLineProcessOutput(process);
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current Git revision", e);
        }
    }

    @Override
    public void updateVcsLog(Path vcsRootPath, Path vcsLogPath, String latestVcsLogRevision, String currentVcsRevision)
    {

    }
}
