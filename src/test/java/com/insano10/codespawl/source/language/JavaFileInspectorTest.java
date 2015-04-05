package com.insano10.codespawl.source.language;

import com.insano10.codespawl.source.CodeUnit;
import com.insano10.codespawl.source.Language;
import com.insano10.codesprawl.util.Utils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileInspectorTest
{
    private static final Path PROJECT_ROOT = Utils.getPathForResource("testProject");

    final JavaFileInspector inspector = new JavaFileInspector();

    @Test
    public void shouldFindCodeUnits() throws Exception
    {
        //given
        final CodeUnit anExpectedCodeUnit = new CodeUnit("com/insano10/test/service/shopping/domain", "Item", 5, Language.JAVA);

        //when
        Collection<CodeUnit> codeUnits = inspector.getCodeUnitsIn(PROJECT_ROOT);

        //then
        assertThat(codeUnits).hasSize(9);
        assertThat(codeUnits).contains(anExpectedCodeUnit);
    }

}