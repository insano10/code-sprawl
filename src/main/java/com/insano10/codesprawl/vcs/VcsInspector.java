package com.insano10.codesprawl.vcs;

import com.insano10.codesprawl.configuration.ConfigurationChangeListener;
import com.insano10.codesprawl.servlets.ProjectConfiguration;
import com.insano10.codesprawl.vcs.control.GitVcsControl;
import com.insano10.codesprawl.vcs.control.SVNVcsControl;
import com.insano10.codesprawl.vcs.control.VcsControl;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class VcsInspector implements ConfigurationChangeListener
{
    private static final Logger LOGGER = Logger.getLogger(VcsInspector.class);

    private VcsControl vcsControl;
    private Path vcsRootPath;
    private Path vcsLogPath;

    public VcsInspector(Path dataDirectory)
    {
        this.vcsLogPath = dataDirectory.resolve("vcs.log");
    }

    @Override
    public void onConfigurationUpdated(ProjectConfiguration configuration)
    {
        this.vcsRootPath = configuration.getVcsRootPath();

        switch(configuration.getVcsOption())
        {
            case SVN: vcsControl = new SVNVcsControl(); break;
            case Git: vcsControl = new GitVcsControl(); break;
            default: LOGGER.error("Unsupported VCS system: " + configuration.getVcsOption());
        }
    }

    public VcsTimeLine getVcsTimeLine()
    {
        updateVcsLog();
        return vcsControl.buildVcsTimeLine();
    }

    private void updateVcsLog()
    {
        if(!Files.exists(vcsLogPath))
        {
            vcsControl.generateVcsLog(vcsRootPath, vcsLogPath);
        }
        else
        {
            String latestVcsLogRevision = vcsControl.getLatestVcsLogRevision(vcsLogPath);
            String currentVcsRevision = vcsControl.getCurrentVcsRevision(vcsRootPath);

            if(!currentVcsRevision.equals(latestVcsLogRevision))
            {
                vcsControl.updateVcsLog(vcsRootPath, vcsLogPath, latestVcsLogRevision, currentVcsRevision);
            }
        }
    }

}
