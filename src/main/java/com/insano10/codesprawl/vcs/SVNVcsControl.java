package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class SVNVcsControl implements VcsControl
{
    private static final Logger LOGGER = Logger.getLogger(SVNVcsControl.class);

    @Override
    public void generateVcsLog(Path vcsRootPath, Path vcsLogPath)
    {
        LOGGER.info("generating SVN log at: " + vcsLogPath);

        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cd " + vcsRootPath + "; svn log --verbose > " + vcsLogPath});
            process.waitFor();

        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.error("Failed to generate SVN log from: " + vcsRootPath + " to: " + vcsLogPath);
        }
    }

    @Override
    public String getCurrentVcsRevision(Path vcsRootPath)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cd " + vcsRootPath + "; svn info | grep 'Revision: ' | cut -d' ' -f2"});
            process.waitFor();

            return ProcessUtils.getSingleLineProcessOutput(process);
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current SVN revision", e);
        }
    }

    @Override
    public String getLatestVcsLogRevision(Path vcsLogPath)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "head -n2 " + vcsLogPath + " | grep -v '^\\-' | cut -d' ' -f1 | cut -c 2-"});
            process.waitFor();

            return ProcessUtils.getSingleLineProcessOutput(process);
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current Git revision", e);
        }
    }
}
