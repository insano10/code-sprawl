package com.insano10.codesprawl.vcs.control;

import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SVNVcsControlTest
{
    private static final Path vcsLogPath = Paths.get("src/test/resources/vcsLogs/svn/vcs.log");

    private final SVNVcsControl control = new SVNVcsControl();

    @Test
    public void shouldBuildVcsTimeLine() throws Exception
    {
        //given
        ProjectFile projectFileWithExtension = new ProjectFile("trunk/coregrind", "m_signals", 0L, "c");
        ProjectFile projectFileWithoutExtension = new ProjectFile("trunk", "NEWS", 0L, null);

        //when
        VcsTimeLine timeLine = control.buildVcsTimeLine(vcsLogPath);

        //then
        assertThat(timeLine.getUserWhoLastModified(projectFileWithExtension)).isEqualTo("philippe");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithExtension)).isEqualTo(1431869905000L);

        assertThat(timeLine.getUserWhoLastModified(projectFileWithoutExtension)).isEqualTo("rhyskidd");
        assertThat(timeLine.getLastModifiedTimeMillis(projectFileWithoutExtension)).isEqualTo(1431871354000L);
    }

}