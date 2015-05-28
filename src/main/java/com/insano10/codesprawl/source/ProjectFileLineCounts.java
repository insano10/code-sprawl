package com.insano10.codesprawl.source;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectFileLineCounts
{
    private final Map<String, Long> lineCounts = new ConcurrentHashMap<>();

    public long getCountForFileExtension(String fileExtension)
    {
        return lineCounts.computeIfAbsent(fileExtension, key -> 0L);
    }

    public void reset()
    {
        lineCounts.clear();
    }

    public void addLineCountForFileExtension(String fileExtension, long lineCount)
    {
        lineCounts.compute(fileExtension, (key, value) -> value == null ? lineCount : value + lineCount);
    }
}
