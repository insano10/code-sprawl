package com.insano10.codesprawl.configuration;

import com.insano10.codesprawl.servlets.ProjectConfiguration;

public interface ConfigurationChangeListener
{
    void onConfigurationUpdated(ProjectConfiguration configuration);
}
