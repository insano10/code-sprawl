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

import static com.insano10.codesprawl.vcs.history.LogParserUtils.*;

public class GitLogParser
{
    private static final Logger LOGGER = Logger.getLogger(GitLogParser.class);
    private static final SimpleDateFormat TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");

    public static List<FileVcsHistory> parse(Path vcsLogFile, String relativeSourceDirectory)
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
                else if (line.trim().isEmpty() && !currentGroup.isMissingTimestamp() && !currentGroup.hasSeenModifiedFiles())
                {
                    currentGroup.modifiedFilesIncoming();
                }
                else if (line.trim().isEmpty() && currentGroup.hasSeenModifiedFiles())
                {
                    currentGroup.modifiedFilesComplete();
                }
                else if(line.matches("^ +.*"))
                {
                    //commit message
                }
                else if (currentGroup.isReadingModifiedFiles())
                {
                    parseModifiedFileIntoGroup(line, currentGroup, relativeSourceDirectory);
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
        final Matcher matcher = getMatcherFor("^commit (.*)", line);
        final String revision = matcher.group(1).trim();
        group.setRevision(revision);
    }

    private static void parseAuthorLineIntoGroup(final String line, final CommitGroup group)
    {
        final Matcher matcher = getMatcherFor("^Author: (.*) <.*", line);
        final String user = matcher.group(1).trim();
        group.setUser(user);
    }

    private static void parseDateLineIntoGroup(final String line, final CommitGroup group)
    {
        final Matcher matcher = getMatcherFor("^Date:(.*)", line);
        final String timestamp = matcher.group(1).trim();

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

    private static void parseModifiedFileIntoGroup(final String line, final CommitGroup group, String relativeSourceDirectory)
    {
        if(line.contains("/"))
        {
            final Matcher matcher = getMatcherFor("^(.*)/(.*?)$", line);
            final String groupName = matcher.group(1);
            final String fileNameAndExtension = matcher.group(2);
            group.addModifiedFile(extractGroupName(groupName, relativeSourceDirectory), extractFileName(fileNameAndExtension), extractFileExtension(fileNameAndExtension));
        }
        else
        {
            group.addModifiedFile("", extractFileName(line), extractFileExtension(line));
        }
    }
}
