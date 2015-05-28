package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.servlets.ProjectConfiguration;
import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.source.ProjectFileBuilder;
import com.insano10.codesprawl.source.ProjectFileLineCounts;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class FileInspectorTest
{
    private static final Path PROJECT_VCS_ROOT = Paths.get("src/testProject/");

    private FileInspector inspector;

    @Before
    public void setUp() throws Exception
    {
        inspector = new FileInspector();
        inspector.onConfigurationUpdated(new ProjectConfiguration(PROJECT_VCS_ROOT.toAbsolutePath().toString(), "SVN", "", new String[]{"java"}));
    }

    @Test
    public void shouldFindMultipleFileTypes() throws Exception
    {
        //given
        inspector.onConfigurationUpdated(new ProjectConfiguration(PROJECT_VCS_ROOT.toAbsolutePath().toString(), "SVN", "moduleA", new String[]{"java", "properties"}));

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
        Collection<ProjectFile> projectFiles = inspector.inspectFiles();

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
        Collection<ProjectFile> projectFiles = inspector.inspectFiles();

        //then
        assertThat(projectFiles).hasSize(10);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleA);
        assertThat(projectFiles).contains(anExpectedProjectFileFromModuleB);
    }

    @Test
    public void shouldCountLinesForEachFileType() throws Exception
    {
        //given
        inspector.onConfigurationUpdated(new ProjectConfiguration(PROJECT_VCS_ROOT.toAbsolutePath().toString(), "SVN", "", new String[]{"java", "properties"}));

        //when
        inspector.inspectFiles();

        //then
        final ProjectFileLineCounts lineCounts = inspector.getLineCounts();

        final long javaLineCount = lineCounts.getCountForFileExtension("java");
        final long propertiesLineCount = lineCounts.getCountForFileExtension("properties");
        final long fortranLineCount = lineCounts.getCountForFileExtension("f90");

        assertThat(javaLineCount).isEqualTo(80);
        assertThat(propertiesLineCount).isEqualTo(2);
        assertThat(fortranLineCount).isEqualTo(0);
    }

}