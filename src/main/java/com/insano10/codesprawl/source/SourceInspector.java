package com.insano10.codesprawl.source;

import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SourceInspector
{
    private static final Logger LOGGER = Logger.getLogger(SourceInspector.class);

    private final Collection<FileInspector> fileInspectors;
    private Path sourceRoot;

    public SourceInspector(Collection<FileInspector> fileInspectors)
    {
        this.fileInspectors = fileInspectors;
    }

    public void setPath(Path path)
    {
        this.sourceRoot = path;
    }

    public Collection<CodeUnit> inspect()
    {
        if (sourceRoot != null)
        {
            LOGGER.info("Inspecting: " + sourceRoot);

            List<CodeUnit> allCodeUnits = fileInspectors.stream().
                    map(i -> i.getCodeUnitsIn(sourceRoot)).
                    flatMap(Collection::stream).
                    collect(Collectors.toList());

            LOGGER.info("Found " + allCodeUnits.size() + " Java code units");

            return allCodeUnits;
        }
        else
        {
            LOGGER.warn("No source root set");
        }

        return Collections.emptyList();
    }

}
