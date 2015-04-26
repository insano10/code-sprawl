package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.source.CodeUnit;
import com.insano10.codesprawl.source.CodeUnitBuilder;
import com.insano10.codesprawl.source.Language;
import com.insano10.codesprawl.utils.Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileInspectorTest
{
    private static final Path PROJECT_SOURCE_ROOT = Paths.get("src/testProject/");
    private static final Path PROJECT_CLASS_ROOT = Paths.get("src/testProject/classes");

    private JavaFileInspector inspector;

    @Before
    public void setUp() throws Exception
    {
        final ClassLoader classLoader = new URLClassLoader(new URL[]{PROJECT_CLASS_ROOT.toUri().toURL()});
        inspector = new JavaFileInspector(PROJECT_SOURCE_ROOT, classLoader);
    }

    @Ignore("Used to regenerate the class directory")
    @Test
    public void updateTestProjectClasses() throws Exception
    {
        //First hit Build -> Rebuild
        try
        {
            if (Files.exists(PROJECT_CLASS_ROOT))
            {
                Utils.deleteFolder(PROJECT_CLASS_ROOT);
            }

            Utils.copyFolder(Paths.get("target/test-classes"), PROJECT_CLASS_ROOT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldFindCodeUnitsAcrossMultipleSourceRoots() throws Exception
    {
        //given
        final CodeUnit anExpectedCodeUnitFromModuleA = new CodeUnitBuilder().
                groupName("com/insano10/codesprawl/shoppingApp/service/shopping/domain").
                name("Item").
                lineCount(19).
                totalMethodCount(3).
                publicMethodCount(1).
                language(Language.JAVA).
                createCodeUnit();

        final CodeUnit anExpectedCodeUnitFromModuleB = new CodeUnitBuilder().
                groupName("com/insano10/codesprawl/discoveryApp/objects").
                name("Discoverer").
                lineCount(21).
                totalMethodCount(2).
                publicMethodCount(1).
                language(Language.JAVA).
                createCodeUnit();

        //when
        Collection<CodeUnit> codeUnits = inspector.getCodeUnits();

        //then
        assertThat(codeUnits).hasSize(10);
        assertThat(codeUnits).contains(anExpectedCodeUnitFromModuleA);
        assertThat(codeUnits).contains(anExpectedCodeUnitFromModuleB);
    }


}