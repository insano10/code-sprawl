package com.insano10.codespawl.source.language;

import com.insano10.codespawl.source.CodeUnit;
import com.insano10.codespawl.source.FileInspector;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.insano10.codespawl.source.Language.JAVA;
import static java.util.stream.Collectors.toList;

public class JavaFileInspector implements FileInspector
{
    private static final Logger LOGGER = Logger.getLogger(JavaFileInspector.class);

    @Override
    public Collection<CodeUnit> getCodeUnitsIn(Path projectPath)
    {
        final Path languageRoot = getRootDirectoryIn(projectPath);

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:***.java");
        final List<Path> matchingJavaPaths = getMatchingPathsIn(languageRoot, pathMatcher);

        final List<CodeUnit> codeUnits = matchingJavaPaths.
                stream().
                map(p -> convert(p, languageRoot)).
                collect(toList());

        return codeUnits;
    }

    private Path getRootDirectoryIn(Path projectPath)
    {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/src/main/java");
        final List<Path> matchingJavaPaths = getMatchingPathsIn(projectPath, pathMatcher);

        if (matchingJavaPaths.isEmpty())
        {
            return null;
        }

        if (matchingJavaPaths.size() > 1)
        {
            throw new RuntimeException("Found " + matchingJavaPaths.size() + " java paths. Only expected a maximum of 1");
        }

        return matchingJavaPaths.get(0);
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

    private CodeUnit convert(final Path codeUnitPath, Path languageRoot)
    {
        final String groupName = languageRoot.relativize(codeUnitPath.getParent()).toString();
        final String name = getCodeUnitName(codeUnitPath);
        final int lineCount = getLineCount(codeUnitPath);

        return new CodeUnit(groupName, name, lineCount, JAVA);
    }

    private int getLineCount(final Path codeUnitPath)
    {
        try
        {
            return Files.readAllLines(codeUnitPath).size();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to read lines from " + codeUnitPath.getFileName(), e);
        }
        return 0;
    }

    private String getCodeUnitName(Path codeUnitPath)
    {
        return codeUnitPath.getFileName().toString().split(".java")[0];
    }
}
