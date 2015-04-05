package com.insano10.codespawl.source;

import com.insano10.codesprawl.util.Utils;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.insano10.codespawl.source.Language.JAVA;
import static com.insano10.codespawl.source.Language.JAVASCRIPT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SourceInspectorTest
{
    private static final Path PROJECT_ROOT = Paths.get("foo");

    private final FileInspector inspector1 = mock(FileInspector.class);
    private final FileInspector inspector2 = mock(FileInspector.class);

    private final SourceInspector inspector = new SourceInspector(Arrays.asList(inspector1, inspector2));


    @Test
    public void shouldGetAllCodeUnits() throws Exception
    {
        //given
        List<CodeUnit> javaUnits = Arrays.asList(
                new CodeUnit("group1", "name1", 10, JAVA),
                new CodeUnit("group1", "name2", 99, JAVA));

        List<CodeUnit> javascriptUnits = Arrays.asList(
                new CodeUnit("group2", "nameA", 5, JAVASCRIPT),
                new CodeUnit("group3", "nameB", 1, JAVASCRIPT),
                new CodeUnit("group3", "nameC", 87, JAVASCRIPT));

        BDDMockito.given(inspector1.getCodeUnitsIn(PROJECT_ROOT)).willReturn(javaUnits);
        BDDMockito.given(inspector2.getCodeUnitsIn(PROJECT_ROOT)).willReturn(javascriptUnits);

        inspector.setPath(PROJECT_ROOT);

        //when
        Collection<CodeUnit> codeUnits = inspector.inspect();

        //then
        assertThat(codeUnits).containsAll(javaUnits).containsAll(javascriptUnits);
    }
}