package com.insano10.codesprawl.vcs.control;

import com.insano10.codesprawl.vcs.VcsUtils;
import com.insano10.codesprawl.vcs.history.FileVcsHistory;
import com.insano10.codesprawl.vcs.history.GitLogParser;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            return VcsUtils.getSingleLineProcessOutput(process);
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

            return VcsUtils.getSingleLineProcessOutput(process);
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException("Failed to get current Git revision", e);
        }
    }

    @Override
    public void updateVcsLog(Path vcsRootPath, Path vcsLogPath, String latestVcsLogRevision, String currentVcsRevision)
    {
        LOGGER.info("generating Git log between revisions: " + latestVcsLogRevision + " and " + currentVcsRevision);

        try
        {
            String historyDelta = "cd " + vcsRootPath + "; git log --name-only  " + latestVcsLogRevision + ".." + currentVcsRevision;
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", historyDelta});
            process.waitFor();

            final List<String> processOutput = VcsUtils.getMultiLineProcessOutput(process);
            final List<String> missingLogLines = getMissingLogLines(processOutput);

            VcsUtils.prependLinesIntoFile(missingLogLines, vcsLogPath);
        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.error("Failed to generate Git log from: " + latestVcsLogRevision + " to: " + currentVcsRevision, e);
        }
    }

    @Override
    public VcsTimeLine buildVcsTimeLine(Path vcsLogPath)
    {
        List<FileVcsHistory> history = GitLogParser.parse(vcsLogPath);
        return new VcsTimeLine(history);
    }

    private List<String> getMissingLogLines(List<String> logDeltaLines)
    {
        //just add a trailing line of whitespace to keep the log consistent
        ArrayList<String> missingLogLines = new ArrayList<>(logDeltaLines);
        missingLogLines.add("");
        return missingLogLines;
    }
}
