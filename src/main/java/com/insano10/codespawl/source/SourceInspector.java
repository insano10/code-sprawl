package com.insano10.codespawl.source;

import org.apache.log4j.Logger;

import java.nio.file.Path;

public class SourceInspector
{
    private static final Logger LOGGER = Logger.getLogger(SourceInspector.class);

    private Path sourceRoot;

    public SourceInspector()
    {

    }

    public void setPath(Path path)
    {
        this.sourceRoot = path;
    }

    public void inspect()
    {
        if(sourceRoot != null)
        {
            LOGGER.info("Inspecting: " + sourceRoot);

        }
        else
        {
            LOGGER.warn("No source root set");
        }
    }
}
