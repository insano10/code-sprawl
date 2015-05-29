package com.insano10.codesprawl.source;

import com.google.gson.internal.LinkedTreeMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectFileLineCounts
{
    private static final int LINE_COUNT_GROUPS = 5;

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

    public Map<String, Long> getAggregateLineCounts()
    {
        final Map<String, Long> sortedMap = new TreeMap<>(new LongValueAscendingComparator<String>(lineCounts));
        sortedMap.putAll(lineCounts);
        final Map<String, Long> aggregateMap = new LinkedHashMap<>();

        for (Map.Entry<String, Long> lineCount : sortedMap.entrySet())
        {
            if(aggregateMap.size() >= LINE_COUNT_GROUPS)
            {
                break;
            }
            aggregateMap.put(lineCount.getKey(), lineCount.getValue());
        }

        //remove these top X entries from the main map
        aggregateMap.keySet().stream().forEach(sortedMap::remove);

        //add up the rest
        long others = sortedMap.values().stream().reduce(0L, (c1, c2) -> c1 + c2);
        aggregateMap.put("Others", others);

        return aggregateMap;
    }
}
