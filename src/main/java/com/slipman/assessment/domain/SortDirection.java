package com.slipman.assessment.domain;

import java.util.Arrays;

import com.slipman.assessment.exception.PostException;

/**
 * Defines a list of allowable directions to sort a {@link java.util.List} of {@link Post}s by when calling
 * {@link com.slipman.assessment.service.HatchwaysAssessmentService#getPosts(String, String, String)}
 */
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

    /**
     * Find a member of this class based on an input sort direction.
     *
     * @param direction the value represented by {@link SortDirection#direction}.
     * @return a member of this enum
     * @throws PostException if no matching enum member is found
     */
    public static SortDirection fromString(String direction)
    {
        return Arrays.stream(values()).filter(sortDirection -> sortDirection.direction.equals(direction)).findFirst()
                .orElseThrow(PostException::getInvalidDirectionException);
    }
}
