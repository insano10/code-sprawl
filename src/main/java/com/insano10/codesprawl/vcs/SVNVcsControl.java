package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SVNVcsControl implements VcsControl
{
    private static final Logger LOGGER = Logger.getLogger(SVNVcsControl.class);
    private final Path vcsRootPath;
    private final Path vcsLogPath;

    public SVNVcsControl(Path vcsRootPath, Path vcsLogPath)
    {
        this.vcsRootPath = vcsRootPath;
        this.vcsLogPath = vcsLogPath;
    }

    @Override
    public void generateVcsLog(Path logPath)
    {
        LOGGER.info("generating SVN log at: " + logPath);

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
    public String getCurrentVcsRevision()
    {
        try
        {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "cd " + vcsRootPath + "; svn info | grep 'Revision: ' | cut -d' ' -f2"});
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current SVN revision", e);
        }
    }

    @Override
    public String getLatestVcsLogRevision()
    {
        return "";
    }
}
