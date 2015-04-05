package com.insano10.codespawl.source;

public class CodeUnit
{
    private final String groupName;
    private final String name;
    private final int lineCount;
    private final Language language;

    public CodeUnit(String groupName, String name, int lineCount, Language language)
    {
        this.groupName = groupName;
        this.name = name;
        this.lineCount = lineCount;
        this.language = language;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeUnit codeUnit = (CodeUnit) o;

        if (lineCount != codeUnit.lineCount) return false;
        if (!groupName.equals(codeUnit.groupName)) return false;
        if (language != codeUnit.language) return false;
        if (!name.equals(codeUnit.name)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = groupName.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + lineCount;
        result = 31 * result + language.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "CodeUnit{" +
                "groupName='" + groupName + '\'' +
                ", name='" + name + '\'' +
                ", lineCount=" + lineCount +
                ", language=" + language +
                '}';
    }
}
