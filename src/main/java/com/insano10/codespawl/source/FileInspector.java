package com.insano10.codespawl.source;

import java.nio.file.Path;
import java.util.Collection;

public interface FileInspector
{
    Collection<CodeUnit> getCodeUnitsIn(Path projectPath);
}
