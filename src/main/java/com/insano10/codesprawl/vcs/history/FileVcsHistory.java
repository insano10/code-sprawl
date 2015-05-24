package com.insano10.codesprawl.vcs.history;

import com.insano10.codesprawl.source.ProjectFile;

public class FileVcsHistory
{
    private final String groupName;
    private final String fileName;
    private final String fileExtension;
    private final String lastModifiedByUser;
    private final long lastModifiedTimeMillis;

    public FileVcsHistory(String groupName, String fileName, String fileExtension, String lastModifiedByUser, long lastModifiedTimeMillis)
    {
        this.groupName = groupName;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.lastModifiedByUser = lastModifiedByUser;
        this.lastModifiedTimeMillis = lastModifiedTimeMillis;
    }

    public String getId()
    {
        return constructId(groupName, fileName, fileExtension);
    }

    public String getLastUser()
    {
        return lastModifiedByUser;
    }

    public long getLastModifiedTimeMillis()
    {
        return lastModifiedTimeMillis;
    }

    public static String getIdFor(ProjectFile file)
    {
        return constructId(file.getGroupName(), file.getName(), file.getFileExtension());
    }

    private static String constructId(String groupName, String fileName, String fileExtension)
    {
        return groupName + ":" + fileName + "." + fileExtension;
    }
}
