package com.insano10.codesprawl.vcs;

import com.insano10.codesprawl.configuration.ConfigurationChangeListener;
import com.insano10.codesprawl.servlets.ProjectConfiguration;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class VcsInspector implements ConfigurationChangeListener
{
    private static final Logger LOGGER = Logger.getLogger(VcsInspector.class);

    private VcsControl vcsControl;
    private Path vcsLogPath;

    public VcsInspector(Path dataDirectory)
    {
        this.vcsLogPath = dataDirectory.resolve("vcs.log");
    }

    @Override
    public void onConfigurationUpdated(ProjectConfiguration configuration)
    {
        Path vcsRootPath = configuration.getVcsRootPath();

        switch(configuration.getVcsOption())
        {
            case SVN: vcsControl = new SVNVcsControl(vcsRootPath); break;
            case Git: vcsControl = new GitVcsControl(vcsRootPath, vcsLogPath); break;
            default: LOGGER.error("Unsupported VCS system: " + configuration.getVcsOption());
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
            String latestVcsLogRevision = vcsControl.getLatestVcsLogRevision(vcsLogPath);
            String currentVcsRevision = vcsControl.getCurrentVcsRevision();

            if(!currentVcsRevision.equals(latestVcsLogRevision))
            {
                vcsControl.generateVcsLog(vcsLogPath);
            }
        }
    }
}
