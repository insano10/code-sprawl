package com.insano10.codesprawl.source;

import com.insano10.codesprawl.configuration.ConfigurationChangeListener;
import com.insano10.codesprawl.servlets.ProjectConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class FileInspector implements ConfigurationChangeListener
{
    private static final Logger LOGGER = Logger.getLogger(FileInspector.class);
    private static final Set<String> EXCLUDED_MIME_TYPES = new HashSet<>();
    private final ProjectFileLineCounts lineCounts;
    private Path sourcePath;
    private String[] fileExtensions;

    public FileInspector()
    {
        this.lineCounts = new ProjectFileLineCounts();
        EXCLUDED_MIME_TYPES.add("image.*");
        EXCLUDED_MIME_TYPES.add("application/gzip");
    }

    @Override
    public void onConfigurationUpdated(ProjectConfiguration configuration)
    {
        this.sourcePath = configuration.getSourceDirectoryPath();
        this.fileExtensions = configuration.getFileExtensions();
    }

    public Collection<ProjectFile> inspectFiles()
    {
        lineCounts.reset();
        String fileExtensionPattern = fileExtensions != null ? String.join(",", fileExtensions) : "";
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(String.format("glob:***.{%s}", fileExtensionPattern));

        return collectProjectFiles(sourcePath, pathMatcher);
    }

    public List<ProjectExtensionLineCount> getAggregateLineCounts()
    {
        return lineCounts.getAggregateLineCounts();
    }

    private List<ProjectFile> collectProjectFiles(Path sourceRoot, PathMatcher pathMatcher)
    {
        final List<Path> matchingJavaPaths = getMatchingPathsIn(sourceRoot, pathMatcher);

        return matchingJavaPaths.parallelStream().
                map(p -> convert(p, sourceRoot)).
                collect(toList());
    }

    private List<Path> getMatchingPathsIn(final Path rootPath, final PathMatcher pathMatcher)
    {
        final List<Path> matchingPaths = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootPath))
        {
            directoryStream.forEach(path -> {

                if (pathMatcher.matches(path) && isAcceptedFileType(path))
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

    private boolean isAcceptedFileType(Path path)
    {
        try
        {
            if(path.toFile().isFile())
            {
                final String contentType = Files.probeContentType(path);
                for (String excludedMimeType : EXCLUDED_MIME_TYPES)
                {
                    if (contentType.matches(excludedMimeType))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to determine if " + path + " is an acceptable type.", e);
        }

        return false;
    }

    private ProjectFile convert(final Path file, Path sourceDirectory)
    {
        final ProjectFileBuilder projectFileBuilder = new ProjectFileBuilder().fileExtension(getFileType(file));

        projectFileBuilder.groupName(sourceDirectory.relativize(file.getParent()).toString());
        projectFileBuilder.name(getFileName(file));
        projectFileBuilder.lineCount(getLineCount(file));

        return projectFileBuilder.createProjectFile();
    }

    private long getLineCount(final Path file)
    {
        try
        {
            long lineCount = Files.readAllLines(file, Charset.forName("UTF-8")).size();
            lineCounts.addLineCountForFileExtension(getFileType(file), lineCount);

            return lineCount;
        }
        catch (IOException e)
        {
            LOGGER.warn("Failed to count lines from " + file.getFileName());
        }
        return 0;
    }

    private String getFileName(Path file)
    {
        String[] tokens = file.getFileName().toString().split("\\.");
        return String.join(".", Arrays.copyOfRange(tokens, 0, tokens.length - 1));
    }

    private String getFileType(Path file)
    {
        String[] tokens = file.getFileName().toString().split("\\.");
        return tokens[tokens.length - 1];
    }
}
