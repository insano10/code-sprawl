package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.nio.file.Path;

public class SVNVcsControl implements VcsControl
{
    private static final Logger LOGGER = Logger.getLogger(SVNVcsControl.class);

    public SVNVcsControl(Path vcsRootPath, Path vcsLogPath)
    {

    }

    @Override
    public void generateVcsLog(Path logPath)
    {
        LOGGER.info("generating SVN log at: " + logPath);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        CommandLine commandline = CommandLine.parse(command);
//        DefaultExecutor exec = new DefaultExecutor();
//        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
//        exec.setStreamHandler(streamHandler);
//        exec.execute(commandline);
//        return(outputStream.toString());
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
