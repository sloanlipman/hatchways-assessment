package com.slipman.assessment.domain;

public enum SortAttribute
{
    ID("id"),
    LIKES("likes"),
    POPULARITY("popularity"),
    READS("reads");

    private final String name;

    public String getName()
    {
        return name;
    }

    SortAttribute(String name)
    {
        this.name = name;
    }
}
