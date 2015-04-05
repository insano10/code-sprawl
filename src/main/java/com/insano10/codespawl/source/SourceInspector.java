package com.insano10.codespawl.source;

import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public class SourceInspector
{
    private static final Logger LOGGER = Logger.getLogger(SourceInspector.class);

    private final FileInspector javaFileInspector;
    private Path sourceRoot;

    public SourceInspector(FileInspector javaFileInspector)
    {
        this.javaFileInspector = javaFileInspector;
    }

    public void setPath(Path path)
    {
        this.sourceRoot = path;
    }

    public Collection<CodeUnit> inspect()
    {
        if(sourceRoot != null)
        {
            LOGGER.info("Inspecting: " + sourceRoot);

            final Path javaRoot = javaFileInspector.getRootDirectoryIn(sourceRoot);
            Collection<CodeUnit> codeUnits = javaFileInspector.getCodeUnitsIn(javaRoot);

            LOGGER.info("Found " + codeUnits.size() + " Java code units");

            return codeUnits;
        }
        else
        {
            LOGGER.warn("No source root set");
        }

        return Collections.emptyList();
    }

}
