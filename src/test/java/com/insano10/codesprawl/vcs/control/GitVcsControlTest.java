package com.insano10.codesprawl.vcs.control;

import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class GitVcsControlTest
{
    private static final Path vcsLogPath = Paths.get("src/test/resources/vcsLogs/git/vcs.log");

    private final GitVcsControl control = new GitVcsControl();

    @Test
    public void shouldStripOffWhateverIsInFrontOfTheRelativeSourcePathInGroupName() throws Exception
    {
        //given
        ProjectFile projectFileWithExtension = new ProjectFile("com/insano10/codesprawl/vcs", "GitVcsControl", 0L, "java");

        //when
        VcsTimeLine timeLine = control.buildVcsTimeLine(vcsLogPath, "com/insano10");

        //then
        assertThat(timeLine.getUserWhoLastModified(projectFileWithExtension)).isEqualTo("insano10");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithExtension)).isEqualTo(1431878979000L);
    }

    @Test
    public void shouldBuildVcsTimeLine() throws Exception
    {
        //given
        ProjectFile projectFileWithExtension = new ProjectFile("src/main/java/com/insano10/codesprawl/vcs", "GitVcsControl", 0L, "java");
        ProjectFile projectFileWithoutExtension = new ProjectFile("node_modules/karma/node_modules/useragent", "LICENSE", 0L, "");
        ProjectFile projectFileWithNoGroupName = new ProjectFile("", "code-sprawl", 0L, "iml");

        //when
        VcsTimeLine timeLine = control.buildVcsTimeLine(vcsLogPath, "");

        //then
        assertThat(timeLine.getUserWhoLastModified(projectFileWithExtension)).isEqualTo("insano10");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithExtension)).isEqualTo(1431878979000L);

        assertThat(timeLine.getUserWhoLastModified(projectFileWithoutExtension)).isEqualTo("insano10");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithoutExtension)).isEqualTo(1425690495000L);

        assertThat(timeLine.getUserWhoLastModified(projectFileWithNoGroupName)).isEqualTo("insano10");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithNoGroupName)).isEqualTo(1431800983000L);
    }

}