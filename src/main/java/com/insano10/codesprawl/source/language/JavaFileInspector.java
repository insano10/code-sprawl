package com.insano10.codesprawl.source.language;

import com.insano10.codesprawl.source.CodeUnit;
import com.insano10.codesprawl.source.CodeUnitBuilder;
import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.Language;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class JavaFileInspector implements FileInspector
{
    private static final Logger LOGGER = Logger.getLogger(JavaFileInspector.class);
    private final Path sourcePath;
    private final ClassLoader classLoader;

    public JavaFileInspector(Path sourcePath, ClassLoader classLoader)
    {
        this.sourcePath = sourcePath;
        this.classLoader = classLoader;
    }

    @Override
    public Collection<CodeUnit> getCodeUnits()
    {
        final Collection<Path> languageRoots = getRootDirectoriesIn(sourcePath);

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:***.java");

        List<CodeUnit> codeUnits = languageRoots.stream().
                map(r -> getCodeUnitsIn(r, pathMatcher)).
                flatMap(Collection::stream).
                collect(toList());

        return codeUnits;
    }

    private List<CodeUnit> getCodeUnitsIn(Path sourceRoot, PathMatcher pathMatcher)
    {
        final List<Path> matchingJavaPaths = getMatchingPathsIn(sourceRoot, pathMatcher);

        return matchingJavaPaths.
                stream().
                map(p -> convert(p, sourceRoot, classLoader)).
                collect(toList());
    }

    private Collection<Path> getRootDirectoriesIn(Path projectPath)
    {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/src/main/java");

        return getMatchingPathsIn(projectPath, pathMatcher);
    }

    private List<Path> getMatchingPathsIn(final Path rootPath, final PathMatcher pathMatcher)
    {
        final List<Path> matchingPaths = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootPath))
        {
            directoryStream.forEach(path -> {

                if (pathMatcher.matches(path))
                {
                    matchingPaths.add(path);
                }
                else
                {
                    if (Files.isDirectory(path))
                    {
                        matchingPaths.addAll(getMatchingPathsIn(path, pathMatcher));
                    }
                }
            });

        }
        catch (IOException e)
        {
            throw new RuntimeException("Error finding path", e);
        }

        return matchingPaths;
    }

    private CodeUnit convert(final Path codeUnitPath, Path languageRoot, ClassLoader classLoader)
    {
        final CodeUnitBuilder codeUnitBuilder = new CodeUnitBuilder().language(Language.JAVA);

        codeUnitBuilder.groupName(languageRoot.relativize(codeUnitPath.getParent()).toString());
        codeUnitBuilder.name(getCodeUnitName(codeUnitPath));
        codeUnitBuilder.lineCount(getLineCount(codeUnitPath));
        populateMethodCounts(codeUnitPath, classLoader, codeUnitBuilder);

        return codeUnitBuilder.createCodeUnit();
    }

    private int getLineCount(final Path codeUnitPath)
    {
        try
        {
            return Files.readAllLines(codeUnitPath, Charset.forName("UTF-8")).size();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to read lines from " + codeUnitPath.getFileName(), e);
        }
        return 0;
    }

    private void populateMethodCounts(final Path codeUnitPath, final ClassLoader classLoader, CodeUnitBuilder codeUnitBuilder)
    {
        final String className = getCodeUnitFullyQualifiedClassName(codeUnitPath);
        try
        {
            Class<?> codeUnitClass = classLoader.loadClass(className);

            long publicMethodCount = Arrays.asList(codeUnitClass.getDeclaredMethods()).stream().filter(m -> Modifier.isPublic(m.getModifiers())).count();
            long totalMethodCount = codeUnitClass.getDeclaredMethods().length;

            codeUnitBuilder.publicMethodCount(publicMethodCount);
            codeUnitBuilder.totalMethodCount(totalMethodCount);
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error("Cannot find class: " + className);
        }
    }

    private String getCodeUnitName(Path codeUnitPath)
    {
        return codeUnitPath.getFileName().toString().split(".java")[0];
    }

    private String getCodeUnitFullyQualifiedClassName(Path codeUnitPath)
    {
        final String classWithSlashesAndFileExtension = codeUnitPath.toAbsolutePath().toString().split("/src/main/java/")[1];
        String classWithFileExtension = classWithSlashesAndFileExtension.replace("/", ".");
        return classWithFileExtension.replace(".java", "");
    }
}
