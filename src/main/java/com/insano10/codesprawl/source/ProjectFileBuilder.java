package com.insano10.codesprawl.source;

public class ProjectFileBuilder
{
    private String groupName = "Unknown";
    private String name = "Unknown";
    private long lineCount = 0;
    private Language language = null;

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

    public ProjectFileBuilder language(Language language)
    {
        this.language = language;
        return this;
    }

    public ProjectFile createCodeUnit()
    {
        return new ProjectFile(groupName, name, lineCount, language);
    }
}