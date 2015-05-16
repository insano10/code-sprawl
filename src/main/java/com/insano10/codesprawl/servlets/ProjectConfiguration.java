package com.insano10.codesprawl.servlets;

import com.insano10.codesprawl.source.VcsSystem;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectConfiguration
{
    private String vcsRoot;
    private String vcsOption;
    private String sourceDirectory;
    private String[] fileExtensions;

    public VcsSystem getVcsOption()
    {
        return VcsSystem.valueOf(vcsOption);
    }

    public Path getVcsRootPath()
    {
        return Paths.get(vcsRoot);
    }

    public String[] getFileExtensions()
    {
        return fileExtensions;
    }

    public Path getSourceDirectoryPath()
    {
        return Paths.get(sourceDirectory);
    }
}
