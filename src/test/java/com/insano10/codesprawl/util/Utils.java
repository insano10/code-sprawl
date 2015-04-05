package com.insano10.codesprawl.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils
{
    public static Path getPathForResource(final String resourcePath)
    {
        try
        {
            final URL resourceURL = Thread.currentThread().getContextClassLoader().getResource(resourcePath);

            if (resourceURL == null)
            {
                throw new RuntimeException("Cannot find " + resourcePath + " on the classpath");
            }

            return Paths.get(resourceURL.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException("Failed to get resource", e);
        }
    }
}
