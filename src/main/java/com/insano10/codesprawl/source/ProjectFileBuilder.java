package com.insano10.codesprawl.source;

public class ProjectFileBuilder
{
    private static final String UNKNOWN = "Unknown";

    private String fileExtension = UNKNOWN;
    private String groupName = UNKNOWN;
    private String name = UNKNOWN;
    private long lineCount = 0;

    public ProjectFileBuilder groupName(String groupName)
    {
        this.groupName = groupName;
        return this;
    }

    public ProjectFileBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public ProjectFileBuilder lineCount(long lineCount)
    {
        this.lineCount = lineCount;
        return this;
    }

    public ProjectFileBuilder fileExtension(String extension)
    {
        this.fileExtension = extension;
        return this;
    }

    public ProjectFile createProjectFile()
    {
        return new ProjectFile(groupName, name, lineCount, fileExtension);
    }
}