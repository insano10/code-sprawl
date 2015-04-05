package com.insano10.codespawl.source;

import java.nio.file.Path;
import java.util.Collection;

public interface FileInspector
{
    Path getRootDirectoryIn(Path projectPath);

    Collection<CodeUnit> getCodeUnitsIn(Path languageRoot);
}
