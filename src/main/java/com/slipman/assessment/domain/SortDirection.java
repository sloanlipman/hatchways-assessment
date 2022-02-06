package com.slipman.assessment.domain;

public enum SortDirection
{
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String direction;

    public String getDirection()
    {
        return direction;
    }

    SortDirection(String direction)
    {
        this.direction = direction;
    }

}
