package com.insano10.codesprawl.source;

import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SourceInspector
{
    private static final Logger LOGGER = Logger.getLogger(SourceInspector.class);

    private final Collection<FileInspector> fileInspectors = new ArrayList<>();

    public void reset()
    {
        fileInspectors.clear();
    }

    public void addFileInspector(FileInspector fileInspector)
    {
        this.fileInspectors.add(fileInspector);
    }

    public Collection<CodeUnit> inspect()
    {
        List<CodeUnit> allCodeUnits = fileInspectors.stream().
                map(FileInspector::getCodeUnits).
                flatMap(Collection::stream).
                collect(Collectors.toList());

        LOGGER.info("Found " + allCodeUnits.size() + " Java code units");

        return allCodeUnits;

    }

}
