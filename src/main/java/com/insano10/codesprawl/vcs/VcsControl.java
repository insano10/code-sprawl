package com.insano10.codesprawl.vcs;

import java.nio.file.Path;

public interface VcsControl
{
    void generateVcsLog(Path logPath);

    String getCurrentVcsRevision();

    String getLatestVcsLogRevision();
}
