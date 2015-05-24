package com.insano10.codesprawl.vcs.history;

import com.insano10.codesprawl.source.ProjectFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VcsTimeLine
{
    private final Map<String, FileVcsHistory> history;

    public VcsTimeLine(List<FileVcsHistory> fileHistory)
    {
        this.history = new ConcurrentHashMap<>();

        for (FileVcsHistory file : fileHistory)
        {
            history.put(file.getId(), file);
        }
    }

    public String getUserWhoLastModified(ProjectFile file)
    {
        final FileVcsHistory fileVcsHistory = getFileVcsHistory(file);
        return (fileVcsHistory == null) ? "" : fileVcsHistory.getLastUser();
    }

    public long getLastModifiedTimeMillis(ProjectFile file)
    {
        final FileVcsHistory fileVcsHistory = getFileVcsHistory(file);
        return (fileVcsHistory == null) ? 0L : fileVcsHistory.getLastModifiedTimeMillis();
    }

    private FileVcsHistory getFileVcsHistory(ProjectFile file)
    {
        return history.get(FileVcsHistory.getIdFor(file));
    }
}
