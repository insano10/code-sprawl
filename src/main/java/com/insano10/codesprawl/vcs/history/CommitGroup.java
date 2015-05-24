package com.insano10.codesprawl.vcs.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommitGroup
{
    private String revision;
    private String user;
    private long timestampMillis = -1;
    private boolean modifiedFilesIncoming;
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
        this.modifiedFilesSeen = true;
    }

    public boolean isMissingRevision()
    {
        return revision == null;
    }

    public boolean isMissingTimestamp()
    {
        return timestampMillis == -1;
    }

    public boolean isReadingModifiedFiles()
    {
        return modifiedFilesIncoming && !modifiedFilesComplete;
    }

    public boolean hasSeenModifiedFiles()
    {
        return modifiedFilesSeen;
    }

    public boolean isComplete()
    {
        return revision != null &&
                user != null &&
                timestampMillis != -1 &&
                modifiedFilesIncoming &&
                modifiedFilesComplete &&
                modifiedFilesSeen;
    }

    public void modifiedFilesIncoming()
    {
        this.modifiedFilesIncoming = true;
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
        this.modifiedFilesIncoming = false;
        this.modifiedFilesComplete = false;
        this.modifiedFilesSeen = false;
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
