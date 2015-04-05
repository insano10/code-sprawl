package com.insano10.codespawl.source.language;

import com.insano10.codesprawl.util.Utils;
import org.junit.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileInspectorTest
{
    final JavaFileInspector inspector = new JavaFileInspector();

    @Test
    public void shouldFindTopLevelJavaPackage() throws Exception
    {
        //given
        final Path testProject = Utils.getPathForResource("testProject");
        final Path expectedDirectory = Utils.getPathForResource("testProject/src/main/java");

        //when
        final Path rootDirectory = inspector.getRootDirectoryIn(testProject);

        //then
        assertThat(rootDirectory).isEqualTo(expectedDirectory);
    }

}