package com.insano10.codesprawl.source;

import org.junit.Test;

import java.util.List;
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

        List<ProjectExtensionLineCount> aggregateLineCounts = counts.getAggregateLineCounts();

        assertThat(aggregateLineCounts.get(0)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("G", 70L));
        assertThat(aggregateLineCounts.get(1)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("F", 60L));
        assertThat(aggregateLineCounts.get(2)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("E", 50L));
        assertThat(aggregateLineCounts.get(3)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("D", 40L));
        assertThat(aggregateLineCounts.get(4)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("C", 30L));
        assertThat(aggregateLineCounts.get(5)).isEqualToComparingFieldByField(new ProjectExtensionLineCount("Others", 30L));
    }
}