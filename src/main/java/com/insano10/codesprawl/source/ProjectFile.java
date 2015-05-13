package com.insano10.codesprawl.source;

public class ProjectFile
{
    private final String groupName;
    private final String name;
    private final long lineCount;
    private final String fileExtension;

    ProjectFile(String groupName, String name, long lineCount, String fileExtension)
    {
        this.groupName = groupName;
        this.name = name;
        this.lineCount = lineCount;
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectFile that = (ProjectFile) o;

        if (lineCount != that.lineCount) return false;
        if (fileExtension != null ? !fileExtension.equals(that.fileExtension) : that.fileExtension != null)
            return false;
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = groupName != null ? groupName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (lineCount ^ (lineCount >>> 32));
        result = 31 * result + (fileExtension != null ? fileExtension.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "ProjectFile{" +
                "groupName='" + groupName + '\'' +
                ", name='" + name + '\'' +
                ", lineCount=" + lineCount +
                ", fileExtension=" + fileExtension +
                '}';
    }
}
