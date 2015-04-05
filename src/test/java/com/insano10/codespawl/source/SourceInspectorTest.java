package com.insano10.codespawl.source;

import com.insano10.codesprawl.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SourceInspectorTest
{
    private static final Path PROJECT_PATH = Utils.getPathForResource("testProject");

    private final FileInspector fileInspector = mock(FileInspector.class);
    private final SourceInspector inspector = new SourceInspector(fileInspector);

    @Before
    public void setUp() throws Exception
    {
        inspector.setPath(PROJECT_PATH);
    }

    @Test
    public void shouldReturnRootDirectoryForKnownLanguage() throws Exception
    {
        //given
        Path expectedJavaRootDirectory = Utils.getPathForResource("testProject/src/main/java");
        given(fileInspector.getRootDirectoryIn(PROJECT_PATH)).willReturn(expectedJavaRootDirectory);

        //when
        inspector.inspect();
        final Path javaRootDirectory = inspector.getRootDirectoryForLanguage(Language.JAVA);

        //then
        assertThat(javaRootDirectory).isEqualTo(expectedJavaRootDirectory);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenFindingRootDirectoryForUnKnownLanguage() throws Exception
    {
        inspector.getRootDirectoryForLanguage(Language.JAVASCRIPT);
    }

}