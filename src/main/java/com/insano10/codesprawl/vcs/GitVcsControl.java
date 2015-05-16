package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class GitVcsControl implements VcsControl
{
    private static final Logger LOGGER = Logger.getLogger(GitVcsControl.class);
    private final Path vcsRootPath;
    private final Path vcsLogPath;

    public GitVcsControl(Path vcsRootPath, Path vcsLogPath)
    {
        this.vcsRootPath = vcsRootPath;
        this.vcsLogPath = vcsLogPath;
    }

    @Override
    public void generateVcsLog(Path logPath)
    {
        LOGGER.info("generating Git log at: " + logPath);

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
    public String getCurrentVcsRevision()
    {
        return "";
    }

    @Override
    public String getLatestVcsLogRevision()
    {
        return "";
    }
}
