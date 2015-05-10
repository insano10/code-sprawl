package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFileBuilder;
import com.insano10.codesprawl.source.Language;
import com.insano10.codesprawl.source.ProjectFile;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class FileInspectorTest
{
    private static final Path PROJECT_SOURCE_ROOT = Paths.get("src/testProject/");

    private FileInspector inspector;

    @Before
    public void setUp() throws Exception
    {
        inspector = new FileInspector();
        inspector.setSourcePath(PROJECT_SOURCE_ROOT);
    }

    @Test
    public void shouldFindCodeUnitsAcrossMultipleSourceRoots() throws Exception
    {
        //given
        final ProjectFile anExpectedProjectFileFromModuleA = new ProjectFileBuilder().
                groupName("com/insano10/codesprawl/shoppingApp/service/shopping/domain").
                name("Item").
                lineCount(19).
                language(Language.JAVA).
                createCodeUnit();

        final ProjectFile anExpectedProjectFileFromModuleB = new ProjectFileBuilder().
                groupName("com/insano10/codesprawl/discoveryApp/objects").
                name("Discoverer").
                lineCount(21).
                language(Language.JAVA).
                createCodeUnit();

        //when
        Collection<ProjectFile> projectFiles = inspector.getFiles();

        //then
        assertThat(projectFiles).hasSize(10);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleA);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleB);
    }


}