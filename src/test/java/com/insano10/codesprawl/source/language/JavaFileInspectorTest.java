package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.source.CodeUnit;
import com.insano10.codesprawl.source.Language;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileInspectorTest
{
    private static final Path PROJECT_SOURCE_ROOT = Paths.get("src/testProject/");
    private static final Path PROJECT_CLASS_ROOT = Paths.get("target/test-classes/");

    final JavaFileInspector inspector = new JavaFileInspector(PROJECT_SOURCE_ROOT, PROJECT_CLASS_ROOT);

    @Test
    public void shouldFindCodeUnits() throws Exception
    {
        //given
        final CodeUnit anExpectedCodeUnit = new CodeUnit("com/insano10/codesprawl/shoppingApp/service/shopping/domain", "Item", 15, 0, Language.JAVA);

        //when
        Collection<CodeUnit> codeUnits = inspector.getCodeUnits();

        //then
        assertThat(codeUnits).hasSize(9);
        assertThat(codeUnits).contains(anExpectedCodeUnit);
    }

}