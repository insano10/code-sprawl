package com.insano10.codespawl.source.language;

import com.insano10.codespawl.source.FileInspector;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JavaFileInspector implements FileInspector
{
    @Override
    public Path getRootDirectoryIn(Path projectPath)
    {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/src/main/java");

        List<Path> matchingJavaPaths = getMatchingPathsIn(projectPath, pathMatcher);

        if (matchingJavaPaths.size() != 1)
        {
            throw new RuntimeException("Found " + matchingJavaPaths.size() + " java paths");
        }

        return matchingJavaPaths.get(0);
    }

    private List<Path> getMatchingPathsIn(final Path rootPath, final PathMatcher pathMatcher)
    {
        final List<Path> matchingStandardJavaFolders = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootPath))
        {
            directoryStream.forEach(path -> {

                if (Files.isDirectory(path))
                {
                    if (pathMatcher.matches(path))
                    {
                        matchingStandardJavaFolders.add(path);
                    }
                    else
                    {
                        matchingStandardJavaFolders.addAll(getMatchingPathsIn(path, pathMatcher));
                    }
                }
            });

        }
        catch (IOException e)
        {
            throw new RuntimeException("Error finding java source path", e);
        }

        return matchingStandardJavaFolders;
    }
}
