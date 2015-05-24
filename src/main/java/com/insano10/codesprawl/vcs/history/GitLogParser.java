package com.insano10.codesprawl.vcs.history;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitLogParser
{
    private static final Logger LOGGER = Logger.getLogger(GitLogParser.class);
    private static final SimpleDateFormat TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");

    public static List<FileVcsHistory> parse(Path vcsLogFile)
    {
        final Map<String, FileVcsHistory> history = new HashMap<>();
        CommitGroup currentGroup = new CommitGroup();
        String line;

        try (BufferedReader reader = Files.newBufferedReader(vcsLogFile, Charset.forName("UTF-8")))
        {
            while ((line = reader.readLine()) != null)
            {
                if (line.matches("^commit.*") && !currentGroup.isMissingRevision())
                {
                    currentGroup.mergeIntoHistory(history);
                    currentGroup.reset();

                    parseRevisionLineIntoGroup(line, currentGroup);
                }
                else if (line.matches("^commit.*") && currentGroup.isMissingRevision())
                {
                    parseRevisionLineIntoGroup(line, currentGroup);
                }
                else if (line.matches("^Author:.*"))
                {
                    parseAuthorLineIntoGroup(line, currentGroup);
                }
                else if (line.matches("^Date:.*"))
                {
                    parseDateLineIntoGroup(line, currentGroup);
                }
                else if (line.trim().isEmpty() && !currentGroup.isMissingTimestamp() && !currentGroup.isReadingModifiedFiles())
                {
                    currentGroup.modifiedFilesIncoming();
                }
                else if (line.trim().isEmpty() && currentGroup.hasSeenModifiedFiles())
                {
                    currentGroup.modifiedFilesComplete();
                }
                else if (currentGroup.isReadingModifiedFiles())
                {
                    parseModifiedFileIntoGroup(line, currentGroup);
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to parse SVN log: " + vcsLogFile, e);
            return Collections.emptyList();
        }

        return new ArrayList<>(history.values());
    }

    private static void parseRevisionLineIntoGroup(final String line, final CommitGroup group)
    {
        final Pattern pattern = Pattern.compile("^commit (.*)");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
            String revision = matcher.group(1).trim();
            group.setRevision(revision);
        }
    }

    private static void parseAuthorLineIntoGroup(final String line, final CommitGroup group)
    {
        final Pattern pattern = Pattern.compile("^Author: (.*) <.*");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
            String user = matcher.group(1).trim();
            group.setUser(user);
        }
    }

    private static void parseDateLineIntoGroup(final String line, final CommitGroup group)
    {
        final Pattern pattern = Pattern.compile("^Date:(.*)");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
            String timestamp = matcher.group(1).trim();

            try
            {
                Date date = TIMESTAMP_DATE_FORMAT.parse(timestamp);
                group.setTimestampMillis(date.getTime());
            }
            catch (ParseException e)
            {
                LOGGER.error("Failed to parse timestamp: " + timestamp, e);
            }
        }
    }

    private static void parseModifiedFileIntoGroup(final String line, final CommitGroup group)
    {
        final Pattern pattern = Pattern.compile("^(.*)/(.*?)$");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
            final String groupName = matcher.group(1);
            final String fileNameAndExtension = matcher.group(2);

            group.addModifiedFile(groupName, getFileName(fileNameAndExtension), getFileExtension(fileNameAndExtension));
        }
    }

    private static String getFileExtension(final String fileNameWithExtension)
    {
        if(fileNameWithExtension.contains("."))
        {
            return fileNameWithExtension.split("\\.")[1];
        }
        else
        {
            return "UNKNOWN";
        }
    }

    private static String getFileName(final String fileNameWithExtension)
    {
        if(fileNameWithExtension.contains("."))
        {
            return fileNameWithExtension.split("\\.")[0];
        }
        else
        {
            return fileNameWithExtension;
        }
    }

}
