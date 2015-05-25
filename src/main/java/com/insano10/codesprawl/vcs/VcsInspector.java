package com.insano10.codesprawl.vcs;

import com.insano10.codesprawl.configuration.ConfigurationChangeListener;
import com.insano10.codesprawl.servlets.ProjectConfiguration;
import com.insano10.codesprawl.vcs.control.GitVcsControl;
import com.insano10.codesprawl.vcs.control.SVNVcsControl;
import com.insano10.codesprawl.vcs.control.VcsControl;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Properties;

public class VcsInspector implements ConfigurationChangeListener
{
    private static final Logger LOGGER = Logger.getLogger(VcsInspector.class);
    private static final String PROJECT_ROOT_KEY = "project-root";

    private VcsControl vcsControl;
    private Path vcsRootPath;
    private Path vcsLogPath;
    private Path metaDataPath;

    public VcsInspector(Path dataDirectory)
    {
        this.metaDataPath = dataDirectory.resolve("metadata.properties");
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
        return vcsControl.buildVcsTimeLine(vcsLogPath);
    }

    private void updateVcsLog()
    {
        if(!Files.exists(vcsLogPath) || !currentVcsLogIsForCurrentVcsRoot())
        {
            vcsControl.generateVcsLog(vcsRootPath, vcsLogPath);
        }
        else
        {
            String latestVcsLogRevision = vcsControl.getLatestVcsLogRevision(vcsLogPath);
            String currentVcsRevision = vcsControl.getCurrentVcsRevision(vcsRootPath);

            if(latestVcsLogRevision == null)
            {
                vcsControl.generateVcsLog(vcsRootPath, vcsLogPath);
            }
            else if(!currentVcsRevision.equals(latestVcsLogRevision))
            {
                vcsControl.updateVcsLog(vcsRootPath, vcsLogPath, latestVcsLogRevision, currentVcsRevision);
            }
        }

        updateMetaData();
    }

    private void updateMetaData()
    {
        try(BufferedWriter writer = Files.newBufferedWriter(metaDataPath))
        {
            Properties properties = new Properties();
            properties.setProperty(PROJECT_ROOT_KEY, vcsRootPath.toAbsolutePath().toString());
            properties.setProperty("last-updated", LocalDateTime.now().toString());
            properties.store(writer, "vcs metadata");
        }
        catch(IOException e)
        {
            LOGGER.error("Failed to write metadata.properties", e);
        }
    }

    private boolean currentVcsLogIsForCurrentVcsRoot()
    {
        final Properties properties = new Properties();
        try(BufferedReader reader = Files.newBufferedReader(metaDataPath))
        {
            properties.load(reader);

            return vcsRootPath.toAbsolutePath().toString().equals(properties.getProperty(PROJECT_ROOT_KEY));
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to read metadata.properties", e);
        }

        return false;
    }

}
