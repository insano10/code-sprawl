package com.insano10.codesprawl.vcs.control;

import com.insano10.codesprawl.vcs.history.VcsTimeLine;

import java.nio.file.Path;

public interface VcsControl
{
    void generateVcsLog(Path vcsRootPath, Path logPath);

    String getCurrentVcsRevision(Path vcsRootPath);

    String getLatestVcsLogRevision(Path vcsLogPath);

    void updateVcsLog(Path vcsRootPath, Path vcsLogPath, String latestVcsLogRevision, String currentVcsRevision);

    VcsTimeLine buildVcsTimeLine(Path vcsLogPath, String relativeSourceDirectory);
}
