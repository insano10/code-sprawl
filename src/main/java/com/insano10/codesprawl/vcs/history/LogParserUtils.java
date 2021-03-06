package com.insano10.codesprawl.vcs.history;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParserUtils
{
    public static Matcher getMatcherFor(final String patternRegex, final String lineToMatch)
    {
        final Pattern pattern = Pattern.compile(patternRegex);
        final Matcher matcher = pattern.matcher(lineToMatch);
        if (matcher.matches())
        {
            return matcher;
        }
        throw new IllegalStateException("Matcher [" + patternRegex + "] does not match [" + lineToMatch + "]");
    }

    public static String extractGroupName(final String fullGroupName, final String relativeSourceDirectory)
    {
        if(relativeSourceDirectory.isEmpty())
        {
            return fullGroupName;
        }

        final String[] tokens = fullGroupName.split(relativeSourceDirectory);

        if(tokens.length == 1)
        {
            return relativeSourceDirectory;
        }
        else if(tokens.length >= 2)
        {
            return relativeSourceDirectory + tokens[1];
        }
        else
        {
            return fullGroupName;
        }
    }

    public static String extractFileName(final String fileNameWithExtension)
    {
        if (fileNameWithExtension.contains("."))
        {
            return fileNameWithExtension.split("\\.")[0];
        }
        else
        {
            return fileNameWithExtension;
        }
    }

    public static String extractFileExtension(final String fileNameWithExtension)
    {
        if (fileNameWithExtension.contains("."))
        {
            return fileNameWithExtension.split("\\.")[1].split(" ")[0]; //remove anything trailing after the file extension
        }
        else
        {
            return "UNKNOWN";
        }
    }

}
