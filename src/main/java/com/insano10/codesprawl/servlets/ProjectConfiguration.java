package com.insano10.codesprawl.servlets;

import com.insano10.codesprawl.vcs.VcsSystem;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectConfiguration
{
    private String vcsRoot;
    private String vcsOption;
    private String sourceDirectory;
    private String[] fileExtensions;

    public ProjectConfiguration(String vcsRoot, String vcsOption, String sourceDirectory, String[] fileExtensions)
    {
        this.vcsRoot = vcsRoot;
        this.vcsOption = vcsOption;
        this.sourceDirectory = sourceDirectory;
        this.fileExtensions = fileExtensions;
    }

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
        return Paths.get(vcsRoot, sourceDirectory);
    }

    public String getRelativeSourceDirectory()
    {
        return sourceDirectory;
    }
}
