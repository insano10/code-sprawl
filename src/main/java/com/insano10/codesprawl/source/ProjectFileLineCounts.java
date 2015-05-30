package com.insano10.codesprawl.source;

import java.util.*;
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

    public List<ProjectExtensionLineCount> getAggregateLineCounts()
    {
        final Map<String, Long> sortedMap = new TreeMap<>(new LongValueAscendingComparator<String>(lineCounts));
        sortedMap.putAll(lineCounts);
        final List<ProjectExtensionLineCount> aggregateCounts = new ArrayList<>();

        //store the top 5 entries
        for (Map.Entry<String, Long> lineCount : sortedMap.entrySet())
        {
            if(aggregateCounts.size() >= 5)
            {
                break;
            }
            aggregateCounts.add(new ProjectExtensionLineCount(lineCount.getKey(), lineCount.getValue()));
        }

        //remove these top 5 entries from the main map
        aggregateCounts.stream().forEach(lc -> sortedMap.remove(lc.getLabel()));

        //add up the rest
        long others = sortedMap.values().stream().reduce(0L, (c1, c2) -> c1 + c2);
        aggregateCounts.add(new ProjectExtensionLineCount("Others", others));

        return aggregateCounts;
    }

}
