package com.insano10.codesprawl.vcs;

import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class VcsInspector
{
    private static final Logger LOGGER = Logger.getLogger(VcsInspector.class);

    private Path vcsRootPath;
    private VcsControl vcsControl;
    private Path vcsLogPath;

    public VcsInspector(Path dataDirectory)
    {
        this.vcsLogPath = dataDirectory.resolve("vcs.log");
    }

    public void updateVcsConfiguration(final VcsSystem vcsSystem, final Path vcsRootPath)
    {
        switch(vcsSystem)
        {
            case SVN: vcsControl = new SVNVcsControl(vcsRootPath, vcsLogPath); break;
            case Git: vcsControl = new GitVcsControl(vcsRootPath, vcsLogPath); break;
            default: LOGGER.error("Unsupported VCS system: " + vcsSystem);
        }
    }

    public void inspectVcs()
    {
        if(!Files.exists(vcsLogPath))
        {
            vcsControl.generateVcsLog(vcsLogPath);
        }
        else
        {
            String latestVcsLogRevision = vcsControl.getLatestVcsLogRevision();
            String currentVcsRevision = vcsControl.getCurrentVcsRevision();

            if(!currentVcsRevision.equals(latestVcsLogRevision))
            {
                vcsControl.generateVcsLog(vcsLogPath);
            }
        }
    }
}
