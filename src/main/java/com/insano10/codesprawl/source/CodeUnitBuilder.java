package com.insano10.codesprawl.source;

public class CodeUnitBuilder
{
    private String groupName = "Unknown";
    private String name = "Unknown";
    private long lineCount = 0;
    private Language language = null;

    public CodeUnitBuilder groupName(String groupName)
    {
        this.groupName = groupName;
        return this;
    }

    public CodeUnitBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public CodeUnitBuilder lineCount(long lineCount)
    {
        this.lineCount = lineCount;
        return this;
    }

    public CodeUnitBuilder language(Language language)
    {
        this.language = language;
        return this;
    }

    public CodeUnit createCodeUnit()
    {
        return new CodeUnit(groupName, name, lineCount, language);
    }
}