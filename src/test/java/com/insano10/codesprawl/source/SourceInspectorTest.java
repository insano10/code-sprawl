package com.insano10.codesprawl.source;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.insano10.codesprawl.source.Language.JAVA;
import static com.insano10.codesprawl.source.Language.JAVASCRIPT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SourceInspectorTest
{
    private static final Path PROJECT_ROOT = Paths.get("foo");

    private final FileInspector inspector1 = mock(FileInspector.class);
    private final FileInspector inspector2 = mock(FileInspector.class);

    private final SourceInspector inspector = new SourceInspector();

    @Before
    public void setUp() throws Exception
    {
        inspector.addFileInspector(inspector1);
        inspector.addFileInspector(inspector2);
    }

    @Test
    public void shouldGetAllCodeUnits() throws Exception
    {
        //given
        List<CodeUnit> javaUnits = Arrays.asList(
                new CodeUnit("group1", "name1", 10, 3, JAVA),
                new CodeUnit("group1", "name2", 99, 12, JAVA));

        List<CodeUnit> javascriptUnits = Arrays.asList(
                new CodeUnit("group2", "nameA", 5, 1, JAVASCRIPT),
                new CodeUnit("group3", "nameB", 1, 0, JAVASCRIPT),
                new CodeUnit("group3", "nameC", 87, 4, JAVASCRIPT));

        BDDMockito.given(inspector1.getCodeUnits()).willReturn(javaUnits);
        BDDMockito.given(inspector2.getCodeUnits()).willReturn(javascriptUnits);

        //when
        Collection<CodeUnit> codeUnits = inspector.inspect();

        //then
        assertThat(codeUnits).containsAll(javaUnits).containsAll(javascriptUnits);
    }
}