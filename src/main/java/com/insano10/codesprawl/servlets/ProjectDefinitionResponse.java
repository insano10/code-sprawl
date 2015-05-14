package com.insano10.codesprawl.servlets;

import com.insano10.codesprawl.source.VcsSystem;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectDefinitionResponse
{
    private String vcsRoot;
    private String sourceDirectory;
    private String[] fileExtensions;
    private String vcsOption;

    public String[] getFileExtensions()
    {
        return fileExtensions;
    }

    public VcsSystem getVcsOption()
    {
        return VcsSystem.valueOf(vcsOption);
    }

    public Path getSourceDirectoryPath()
    {
        return Paths.get(sourceDirectory);
    }

    public Path getVcsRootPath()
    {
        return Paths.get(vcsRoot);
    }
}
