package com.insano10.codesprawl.configuration;

import com.insano10.codesprawl.servlets.ProjectConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationManager
{
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class);
    private static final String CONFIG_FILE = "config.properties";

    private final Properties properties = new Properties();
    private Path saveDataPath;

    public void loadConfiguration()
    {
        initialiseSaveDataPath();

        final Path configFile = saveDataPath.resolve(CONFIG_FILE);
        if(Files.exists(configFile))
        {
            try
            {
                properties.load(Files.newBufferedReader(configFile));
            }
            catch(IOException e)
            {
                LOGGER.error("Failed to read: " + configFile);
            }
        }
        else
        {
            createConfigFile(configFile);
        }
    }

    private void createConfigFile(Path configFile)
    {
        try
        {
            if(!Files.exists(saveDataPath))
            {
                Files.createDirectories(saveDataPath);
            }
            Files.createFile(configFile);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to create config file: " + configFile);
        }
    }

    public void saveConfiguration(ProjectConfiguration projectDefinition)
    {
    }

    private void initialiseSaveDataPath()
    {
        final String userHome = System.getProperty("user.home");
        saveDataPath = Paths.get(userHome, ".code-sprawl", "config");

        LOGGER.info("Save data directory is: " + saveDataPath);
    }
}
