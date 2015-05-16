package com.insano10.codesprawl.configuration;

import com.insano10.codesprawl.servlets.ProjectConfiguration;
import com.insano10.codesprawl.vcs.VcsSystem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigurationManager
{
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class);
    private static final String CONFIG_FILE = "config.properties";
    private static final String VCS_OPTION_KEY = "vcsOption";
    private static final String VCS_ROOT_DIR_KEY = "vcsRootDir";
    private static final String VISUALISATION_SOURCE_DIR_KEY = "visualisationSourceDir";
    private static final String VISUALISATION_FILE_EXTENSIONS_KEY = "visualisationFileExtensions";

    private final List<ConfigurationChangeListener> changeListeners = new ArrayList<>();
    private final Properties properties = new Properties();
    private Path saveDataDirPath;
    private Path configFilePath;

    public void addChangeListener(final ConfigurationChangeListener listener)
    {
        changeListeners.add(listener);
    }

    public void loadConfiguration()
    {
        initialiseDataDirectory();

        try
        {
            properties.load(Files.newBufferedReader(configFilePath));
            changeListeners.stream().forEach(l -> l.onConfigurationUpdated(getConfiguration()));

            LOGGER.info("Loaded properties: " + properties);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to read: " + configFilePath);
        }
    }

    public void saveConfiguration(ProjectConfiguration projectConfig)
    {
        properties.setProperty(VCS_OPTION_KEY, projectConfig.getVcsOption().name());
        properties.setProperty(VCS_ROOT_DIR_KEY, projectConfig.getVcsRootPath().toString());
        properties.setProperty(VISUALISATION_SOURCE_DIR_KEY, projectConfig.getRelativeSourceDirectory());
        properties.setProperty(VISUALISATION_FILE_EXTENSIONS_KEY, String.join(",", projectConfig.getFileExtensions()));

        try
        {
            properties.store(Files.newBufferedWriter(saveDataDirPath.resolve(CONFIG_FILE)), "Code Sprawl Properties");

            changeListeners.stream().forEach(l -> l.onConfigurationUpdated(projectConfig));
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to save properties to: " + configFilePath);
        }
    }

    private void createConfigFile(Path configFile)
    {
        try
        {
            if (!Files.exists(saveDataDirPath))
            {
                Files.createDirectories(saveDataDirPath);
            }
            Files.createFile(configFile);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to create config file: " + configFile);
        }
    }

    private void initialiseDataDirectory()
    {
        Path dataDirectory = getDataDirectory();
        saveDataDirPath = dataDirectory.resolve("config");
        configFilePath = saveDataDirPath.resolve(CONFIG_FILE);

        LOGGER.info("Save data directory is: " + saveDataDirPath);

        if (!Files.exists(configFilePath))
        {
            LOGGER.info("Creating new config file: " + configFilePath);
            createConfigFile(configFilePath);
        }
    }

    public ProjectConfiguration getConfiguration()
    {
        return new ProjectConfiguration(properties.getProperty(VCS_ROOT_DIR_KEY),
                                        properties.getProperty(VCS_OPTION_KEY),
                                        properties.getProperty(VISUALISATION_SOURCE_DIR_KEY),
                                        properties.getProperty(VISUALISATION_FILE_EXTENSIONS_KEY).split(","));
    }

    public Path getDataDirectory()
    {
        final String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".code-sprawl");
    }
}
