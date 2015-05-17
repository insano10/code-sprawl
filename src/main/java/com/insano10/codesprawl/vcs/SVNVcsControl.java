package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void updateVcsLog(Path vcsRootPath, Path vcsLogPath, String latestVcsLogRevision, String currentVcsRevision)
    {
        LOGGER.info("generating SVN log between revisions: " + latestVcsLogRevision + " and " + currentVcsRevision);

        try
        {
            String historyDelta = "cd " + vcsRootPath + "; svn log --verbose -r " + currentVcsRevision + ":" + latestVcsLogRevision;
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", historyDelta});
            process.waitFor();

            final List<String> processOutput = ProcessUtils.getMultiLineProcessOutput(process);
            final List<String> missingLogLines = getMissingLogLines(latestVcsLogRevision, processOutput);

            mergeDeltaIntoLogFile(missingLogLines, vcsLogPath);
        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.error("Failed to generate SVN log from: " + vcsRootPath + " to: " + vcsLogPath);
        }
    }

    private void mergeDeltaIntoLogFile(List<String> missingLogLines, Path vcsLogPath) throws IOException
    {
        Path tmpPath = vcsLogPath.getParent().resolve("vcs.log.tmp");

        Files.write(tmpPath, missingLogLines, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(tmpPath, Files.readAllLines(vcsLogPath), StandardOpenOption.APPEND);
        Files.delete(vcsLogPath);
        Files.move(tmpPath, vcsLogPath);
    }

    private List<String> getMissingLogLines(String latestVcsLogRevision, List<String> logDeltaLines)
    {
        //strip off last entry (as we already have this in the log), svn -rA:B is inclusive
        List<String> trimmedLogDelta = new ArrayList<>();
        for(String line : logDeltaLines)
        {
            if(line.matches("r" + latestVcsLogRevision + ".*"))
            {
                break;
            }
            trimmedLogDelta.add(line);
        }
        //trim off the trailing dashed line
        trimmedLogDelta.remove(trimmedLogDelta.size()-1);
        return trimmedLogDelta;
    }
}
