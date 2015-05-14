package com.insano10.codesprawl.source;

public class VcsInspector
{
    private VcsSystem vcsSystem;

    public void setSystem(final VcsSystem vcsSystem)
    {
        this.vcsSystem = vcsSystem;
    }

    public void inspectVcs()
    {
        System.out.println("Vcs System is: " + vcsSystem);
    }
}
