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

import static com.insano10.codesprawl.vcs.history.LogParserUtils.extractFileExtension;
import static com.insano10.codesprawl.vcs.history.LogParserUtils.extractFileName;

public class SVNLogParser
{
    private static final Logger LOGGER = Logger.getLogger(SVNLogParser.class);
    private static final SimpleDateFormat TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    public static List<FileVcsHistory> parse(Path vcsLogFile)
    {
        final Map<String, FileVcsHistory> history = new HashMap<>();
        CommitGroup currentGroup = new CommitGroup();
        String line;

        try (BufferedReader reader = Files.newBufferedReader(vcsLogFile, Charset.forName("UTF-8")))
        {
            while ((line = reader.readLine()) != null)
            {
                if (line.trim().matches("^[-]+$") && currentGroup.isComplete())
                {
                    currentGroup.mergeIntoHistory(history);
                    currentGroup.reset();
                }
                else if (line.matches("^r.*") && currentGroup.isMissingRevision())
                {
                    parseRevisionLineIntoGroup(line, currentGroup);
                }
                else if (line.matches("Changed paths:"))
                {
                    currentGroup.modifiedFilesIncoming();
                }
                else if (line.trim().isEmpty())
                {
                    currentGroup.modifiedFilesComplete();
                }
                else if (currentGroup.isReadingModifiedFiles())
                {
                    parseModifiedFileIntoGroup(line, currentGroup);
                }
                else
                {
                    //commit message
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
        final Matcher matcher = LogParserUtils.getMatcherFor("^r(.*) \\|(.*)\\|(.*) \\(.*\\|.*", line);
        final String revision = matcher.group(1).trim();
        final String user = matcher.group(2).trim();
        final String timestamp = matcher.group(3).trim();

        group.setRevision(revision);
        group.setUser(user);

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

    private static void parseModifiedFileIntoGroup(final String line, final CommitGroup group)
    {
        int numberOfSlashesInLine = countSlashesInLine(line);
        if(numberOfSlashesInLine > 1)
        {
            final Matcher matcher = LogParserUtils.getMatcherFor("^.*?/(.*)/(.*?)$", line);
            final String groupName = matcher.group(1);
            final String fileNameAndExtension = matcher.group(2);

            group.addModifiedFile(groupName, extractFileName(fileNameAndExtension), extractFileExtension(fileNameAndExtension));
        }
        else
        {
            String fileNameAndExtension = line.substring(1);
            group.addModifiedFile("", extractFileName(fileNameAndExtension), extractFileExtension(fileNameAndExtension));
        }
    }

    private static int countSlashesInLine(String line)
    {
        return line.length() - line.replace("/", "").length();
    }
}
