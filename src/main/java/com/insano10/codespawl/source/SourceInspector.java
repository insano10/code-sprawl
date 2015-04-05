package com.insano10.codespawl.source;

import org.apache.log4j.Logger;

import java.nio.file.Path;

import static com.insano10.codespawl.source.Language.JAVA;

public class SourceInspector
{
    private static final Logger LOGGER = Logger.getLogger(SourceInspector.class);

    private final FileInspector javaFileInspector;
    private Path sourceRoot;

    public SourceInspector(FileInspector javaFileInspector)
    {
        this.javaFileInspector = javaFileInspector;
    }

    public void setPath(Path path)
    {
        this.sourceRoot = path;
    }

    public String inspect()
    {
        if(sourceRoot != null)
        {
            LOGGER.info("Inspecting: " + sourceRoot);

            final Path javaRoot = getRootDirectoryForLanguage(JAVA);

            LOGGER.info("Java root is: " + javaRoot);

        }
        else
        {
            LOGGER.warn("No source root set");
        }

        return "foo";
    }

    public Path getRootDirectoryForLanguage(Language language)
    {
        switch (language)
        {
            case JAVA:
                return javaFileInspector.getRootDirectoryIn(sourceRoot);
        }
        throw new RuntimeException("Language " + language + " not yet supported");
    }
}
