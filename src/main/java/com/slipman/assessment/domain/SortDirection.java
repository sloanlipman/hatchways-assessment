package com.slipman.assessment.domain;

import java.util.Arrays;

import com.slipman.assessment.exception.PostException;

public enum SortDirection
{
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String direction;

    public String getDirection()
    {
        return direction;
    }

    public static SortDirection fromString(String direction)
    {
        return Arrays.stream(values()).filter(sortDirection -> sortDirection.direction.equals(direction)).findFirst()
                .orElseThrow(PostException::getInvalidDirectionException);
    }

    SortDirection(String direction)
    {
        this.direction = direction;
    }
}
