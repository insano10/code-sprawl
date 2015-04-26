package com.insano10.codesprawl.discoveryApp.objects;

public class Discoverer
{
    private final String name;

    public Discoverer()
    {
        this.name = "Bob";
    }

    public String getName()
    {
        return mungeName();
    }

    private String mungeName()
    {
        return "Mr " + name;
    }
}
