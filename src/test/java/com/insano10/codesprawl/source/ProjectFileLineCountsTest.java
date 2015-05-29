package com.insano10.codesprawl.source;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectFileLineCountsTest
{

    @Test
    public void shouldConstructAggregateLineCounts() throws Exception
    {

        ProjectFileLineCounts counts = new ProjectFileLineCounts();

        counts.addLineCountForFileExtension("A", 10);
        counts.addLineCountForFileExtension("B", 20);
        counts.addLineCountForFileExtension("C", 30);
        counts.addLineCountForFileExtension("D", 40);
        counts.addLineCountForFileExtension("E", 50);
        counts.addLineCountForFileExtension("F", 60);
        counts.addLineCountForFileExtension("G", 70);

        Map<String, Long> aggregateLineCounts = counts.getAggregateLineCounts();

        assertThat(aggregateLineCounts).containsEntry("G", 70L);
        assertThat(aggregateLineCounts).containsEntry("F", 60L);
        assertThat(aggregateLineCounts).containsEntry("E", 50L);
        assertThat(aggregateLineCounts).containsEntry("D", 40L);
        assertThat(aggregateLineCounts).containsEntry("C", 30L);
        assertThat(aggregateLineCounts).containsEntry("Others", 30L);
    }
}