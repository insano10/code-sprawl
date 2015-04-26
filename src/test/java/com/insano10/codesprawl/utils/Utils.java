package com.insano10.codesprawl.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Utils
{
    public static void deleteFolder(Path folder) throws IOException
    {
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

    public static void copyFolder(Path folderSrc, Path folderDest) throws IOException
    {
        final CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING};

        Files.walkFileTree(folderSrc, new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            {
                Path newDirectory = folderDest.resolve(folderSrc.relativize(dir));
                try
                {
                    Files.copy(dir, newDirectory, copyOptions);
                }
                catch (FileAlreadyExistsException x)
                {
                }
                catch (IOException x)
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                Path destination = folderDest.resolve(folderSrc.relativize(file));
                Files.copy(file, destination, copyOptions);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
