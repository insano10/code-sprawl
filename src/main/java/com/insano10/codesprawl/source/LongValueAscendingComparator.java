package com.insano10.codesprawl.source;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class LongValueAscendingComparator<K> implements Comparator<K>
{
    private final Map<K,Long> base;

    public LongValueAscendingComparator(Map<K, Long> base)
    {
        this.base = Collections.unmodifiableMap(base);
    }

    @Override
    public int compare(K o1, K o2)
    {
        return -base.get(o1).compareTo(base.get(o2));
    }
}
