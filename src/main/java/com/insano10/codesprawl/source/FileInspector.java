package com.insano10.codesprawl.source;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileInspector
{
    private static final Logger LOGGER = Logger.getLogger(FileInspector.class);
    private Path sourcePath;

    public void setSourcePath(final Path sourcePath)
    {
        this.sourcePath = sourcePath;
    }

    public Collection<ProjectFile> getFiles()
    {
        final Collection<Path> languageRoots = getRootDirectoriesIn(sourcePath);

        //todo: get files other than .java
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:***.java");

        List<ProjectFile> files = languageRoots.stream().
                map(r -> getCodeUnitsIn(r, pathMatcher)).
                flatMap(Collection::stream).
                collect(toList());

        return files;
    }

    private List<ProjectFile> getCodeUnitsIn(Path sourceRoot, PathMatcher pathMatcher)
    {
        final List<Path> matchingJavaPaths = getMatchingPathsIn(sourceRoot, pathMatcher);

        return matchingJavaPaths.
                stream().
                map(p -> convert(p, sourceRoot)).
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

    private ProjectFile convert(final Path codeUnitPath, Path languageRoot)
    {
        final ProjectFileBuilder projectFileBuilder = new ProjectFileBuilder().language(Language.JAVA);

        projectFileBuilder.groupName(languageRoot.relativize(codeUnitPath.getParent()).toString());
        projectFileBuilder.name(getCodeUnitName(codeUnitPath));
        projectFileBuilder.lineCount(getLineCount(codeUnitPath));

        return projectFileBuilder.createCodeUnit();
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

    private String getCodeUnitName(Path codeUnitPath)
    {
        return codeUnitPath.getFileName().toString().split(".java")[0];
    }

}
