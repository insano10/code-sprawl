package com.insano10.codesprawl.vcs;

import java.nio.file.Path;

public class GitVcsControl implements VcsControl
{
    public GitVcsControl(Path vcsRootPath, Path vcsLogPath)
    {

    }

    @Override
    public void generateVcsLog(Path logPath)
    {

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
