package com.slipman.assessment.domain;

import java.util.Arrays;

import com.slipman.assessment.exception.PostException;

public enum SortAttribute
{
    ID("id"),
    LIKES("likes"),
    POPULARITY("popularity"),
    READS("reads");

    private final String attributeName;

    public String getAttributeName()
    {
        return attributeName;
    }

    SortAttribute(String name)
    {
        this.attributeName = name;
    }

    public static SortAttribute fromString(String attributeName)
    {
        return Arrays.stream(values()).filter(sortAttribute -> sortAttribute.attributeName.equals(attributeName))
                .findFirst().orElseThrow(PostException::getInvalidSortByException);
    }
}
