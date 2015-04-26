package com.insano10.codesprawl.source;

import java.nio.file.Path;
import java.util.Collection;

public interface FileInspector
{
    Collection<CodeUnit> getCodeUnitsIn(Path projectPath);
}
