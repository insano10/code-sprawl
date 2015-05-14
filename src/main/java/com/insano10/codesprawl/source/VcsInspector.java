package com.insano10.codesprawl.source;

import java.nio.file.Path;

public class VcsInspector
{
    private VcsSystem vcsSystem;
    private Path vcsRootPath;

    public void setSystem(final VcsSystem vcsSystem)
    {
        this.vcsSystem = vcsSystem;
    }

    public void setVcsRoot(Path vcsRootPath)
    {

        this.vcsRootPath = vcsRootPath;
    }

    public void inspectVcs()
    {
        System.out.println("Vcs System is: " + vcsSystem);
        System.out.println("Root is: " + vcsRootPath);
    }
}
