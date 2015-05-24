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
        final Pattern pattern = Pattern.compile("^r(.*) \\|(.*)\\|(.*) \\(.*\\|.*");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
            String revision = matcher.group(1).trim();
            String user = matcher.group(2).trim();
            String timestamp = matcher.group(3).trim();

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
    }

    private static void parseModifiedFileIntoGroup(final String line, final CommitGroup group)
    {
        final Pattern pattern = Pattern.compile("^.*?/(.*)/(.*?)$");
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

    private static class CommitGroup
    {
        private String revision;
        private String user;
        private long timestampMillis = -1;
        private boolean modifiedFilesSeen;
        private boolean modifiedFilesComplete;
        private List<FileVcsHistory> files = new ArrayList<>();

        public void setRevision(String revision)
        {
            this.revision = revision;
        }

        public void setUser(String user)
        {
            this.user = user;
        }

        public void setTimestampMillis(long timestampMillis)
        {
            this.timestampMillis = timestampMillis;
        }

        public void addModifiedFile(String groupName, String fileName, String fileExtension)
        {
            files.add(new FileVcsHistory(groupName, fileName, fileExtension, user, timestampMillis));
        }

        public boolean isMissingRevision()
        {
            return revision == null;
        }

        public boolean isReadingModifiedFiles()
        {
            return modifiedFilesSeen && !modifiedFilesComplete;
        }

        public boolean isComplete()
        {
            return revision != null &&
                    user != null &&
                    timestampMillis != -1 &&
                    modifiedFilesSeen &&
                    modifiedFilesComplete;
        }

        public void modifiedFilesIncoming()
        {
            this.modifiedFilesSeen = true;
        }

        public void modifiedFilesComplete()
        {
            this.modifiedFilesComplete = true;
        }

        public void reset()
        {
            this.revision = null;
            this.user = null;
            this.timestampMillis = -1;
            this.modifiedFilesSeen = false;
            this.modifiedFilesComplete = false;
            this.files.clear();
        }

        public void mergeIntoHistory(Map<String, FileVcsHistory> history)
        {
            //assume we are merging in time descending order, so if it's already in the map, don't replace it
            files.stream().
                    filter(file -> !history.containsKey(file.getId())).
                    forEach(file -> history.put(file.getId(), file));
        }
    }
}
