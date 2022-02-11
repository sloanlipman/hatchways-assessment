package com.slipman.assessment.domain;

import java.util.Arrays;

import com.slipman.assessment.exception.PostException;

/**
 * Defines a list of allowable fields to sort a {@link java.util.List} of {@link Post}s by when calling
 * {@link com.slipman.assessment.service.HatchwaysAssessmentService#getPosts(String, String, String)}
 */
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

    /**
     * Find a member of this class based on an input sort attribute.
     *
     * @param attributeName the value represented by {@link SortAttribute#attributeName}.
     * @return a member of this enum
     * @throws PostException if no matching enum member is found
     */
    public static SortAttribute fromString(String attributeName)
    {
        return Arrays.stream(values()).filter(sortAttribute -> sortAttribute.attributeName.equals(attributeName))
                .findFirst().orElseThrow(PostException::getInvalidSortByException);
    }
}
