package com.insano10.codesprawl.source;

public class ProjectExtensionLineCount
{
    private final String label;
    private final long lineCount;

    public ProjectExtensionLineCount(String label, long lineCount)
    {
        this.label = label;
        this.lineCount = lineCount;
    }

    public String getLabel()
    {
        return label;
    }

    public long getLineCount()
    {
        return lineCount;
    }
}
