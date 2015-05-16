package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.source.ProjectFileBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class FileInspectorTest
{
    private static final Path PROJECT_SOURCE_ROOT = Paths.get("src/testProject/");
    private static final Path PROJECT_MODULE_A_SOURCE_ROOT = Paths.get("src/testProject/moduleA");

    private FileInspector inspector;

    @Before
    public void setUp() throws Exception
    {
        inspector = new FileInspector();
        inspector.updateFileConfiguration(PROJECT_SOURCE_ROOT);
        inspector.setFileExtensions(new String[]{"java"});
    }

    @Test
    public void shouldFindMultipleFileTypes() throws Exception
    {
        //given
        inspector.updateFileConfiguration(PROJECT_MODULE_A_SOURCE_ROOT);
        inspector.setFileExtensions(new String[] {"java", "properties"});

        final ProjectFile anExpectedJavaProjectFile = new ProjectFileBuilder().
                groupName("src/main/java/com/insano10/codesprawl/shoppingApp/service/shopping/domain").
                name("Item").
                lineCount(19).
                fileExtension("java").
                createProjectFile();

        final ProjectFile anExpectedPropertiesProjectFile = new ProjectFileBuilder().
                groupName("src/main/resources").
                name("project").
                lineCount(2).
                fileExtension("properties").
                createProjectFile();

        //when
        Collection<ProjectFile> projectFiles = inspector.getFiles();

        //then
        assertThat(projectFiles).contains(anExpectedJavaProjectFile);
        assertThat(projectFiles).contains(anExpectedPropertiesProjectFile);
    }

    @Test
    public void shouldFindFilesAcrossMultipleSourceRoots() throws Exception
    {
        //given
        final ProjectFile anExpectedProjectFileFromModuleA = new ProjectFileBuilder().
                groupName("moduleA/src/main/java/com/insano10/codesprawl/shoppingApp/service/shopping/domain").
                name("Item").
                lineCount(19).
                fileExtension("java").
                createProjectFile();

        final ProjectFile anExpectedProjectFileFromModuleB = new ProjectFileBuilder().
                groupName("moduleB/src/main/java/com/insano10/codesprawl/discoveryApp/objects").
                name("Discoverer").
                lineCount(21).
                fileExtension("java").
                createProjectFile();

        //when
        Collection<ProjectFile> projectFiles = inspector.getFiles();

        //then
        assertThat(projectFiles).hasSize(10);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleA);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleB);
    }


}