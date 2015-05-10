package com.insano10.codesprawl.source;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.insano10.codesprawl.source.Language.JAVA;
import static com.insano10.codesprawl.source.Language.JAVASCRIPT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SourceInspectorTest
{
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
                new CodeUnitBuilder().groupName("group1").name("name1").lineCount(10).language(JAVA).createCodeUnit(),
                new CodeUnitBuilder().groupName("group1").name("name2").lineCount(99).language(JAVA).createCodeUnit());

        List<CodeUnit> javascriptUnits = Arrays.asList(
                new CodeUnitBuilder().groupName("group2").name("nameA").lineCount(5).language(JAVASCRIPT).createCodeUnit(),
                new CodeUnitBuilder().groupName("group3").name("nameB").lineCount(1).language(JAVASCRIPT).createCodeUnit(),
                new CodeUnitBuilder().groupName("group3").name("nameC").lineCount(87).language(JAVASCRIPT).createCodeUnit());

        BDDMockito.given(inspector1.getCodeUnits()).willReturn(javaUnits);
        BDDMockito.given(inspector2.getCodeUnits()).willReturn(javascriptUnits);

        //when
        Collection<CodeUnit> codeUnits = inspector.inspect();

        //then
        assertThat(codeUnits).containsAll(javaUnits).containsAll(javascriptUnits);
    }
}