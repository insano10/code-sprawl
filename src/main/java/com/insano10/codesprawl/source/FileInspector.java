package com.insano10.codesprawl.source;

import com.insano10.codesprawl.configuration.ConfigurationChangeListener;
import com.insano10.codesprawl.servlets.ProjectConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileInspector implements ConfigurationChangeListener
{
    private static final Logger LOGGER = Logger.getLogger(FileInspector.class);
    private Path sourcePath;
    private String[] fileExtensions;

    @Override
    public void onConfigurationUpdated(ProjectConfiguration configuration)
    {
        this.sourcePath = configuration.getSourceDirectoryPath();
        this.fileExtensions = configuration.getFileExtensions();
    }

    public Collection<ProjectFile> getFiles()
    {
        String fileExtensionPattern = fileExtensions != null ? String.join(",", fileExtensions) : "";
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(String.format("glob:***.{%s}", fileExtensionPattern));

        return getFilesIn(sourcePath, pathMatcher);
    }

    private List<ProjectFile> getFilesIn(Path sourceRoot, PathMatcher pathMatcher)
    {
        final List<Path> matchingJavaPaths = getMatchingPathsIn(sourceRoot, pathMatcher);

        return matchingJavaPaths.
                stream().
                map(p -> convert(p, sourceRoot)).
                collect(toList());
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

    private ProjectFile convert(final Path file, Path sourceDirectory)
    {
        final ProjectFileBuilder projectFileBuilder = new ProjectFileBuilder().fileExtension(getFileType(file));

        projectFileBuilder.groupName(sourceDirectory.relativize(file.getParent()).toString());
        projectFileBuilder.name(getFileName(file));
        projectFileBuilder.lineCount(getLineCount(file));

        return projectFileBuilder.createProjectFile();
    }

    private int getLineCount(final Path file)
    {
        try
        {
            return Files.readAllLines(file, Charset.forName("UTF-8")).size();
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to read lines from " + file.getFileName(), e);
        }
        return 0;
    }

    private String getFileName(Path file)
    {
        return file.getFileName().toString().split("\\.")[0];
    }

    private String getFileType(Path file)
    {
        return file.getFileName().toString().split("\\.")[1];
    }
}
