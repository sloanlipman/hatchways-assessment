package com.slipman.assessment.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.slipman.assessment.domain.SortAttribute;
import com.slipman.assessment.domain.SortDirection;
import com.slipman.assessment.exception.PostException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;

@Component
public class HatchawaysAssessmentService
{
    private static final List<String> ALLOWED_SORT_ATTRIBUTES = Arrays.stream(SortAttribute.values()).map(SortAttribute::getName).collect(Collectors.toList());

    private static final List<String> ALLOWED_SORT_DIRECTIONS = Arrays.stream(SortDirection.values()).map(SortDirection::getDirection).collect(Collectors.toList());

    public Ping ping()
    {
        return new Ping(true);
    }

    /**
     *
     * @param tags
     * @param sortBy
     * @param direction
     * @return
     * @throws
     */
    public List<Post> getPosts(String tags, String sortBy, String direction)
    {
        List<PostException.ErrorCode> errorCodes = new ArrayList<>();
        List<String> tagsList;
        if (StringUtils.isEmpty(tags))
        {
            errorCodes.add(PostException.ErrorCode.TAGS);
        }

        if (StringUtils.isNotEmpty(sortBy) && !ALLOWED_SORT_DIRECTIONS.contains((sortBy)))
        {
            errorCodes.add(PostException.ErrorCode.SORT_BY);
        }

        if (StringUtils.isNotEmpty(direction) && !ALLOWED_SORT_DIRECTIONS.contains((direction)))
        {
            errorCodes.add(PostException.ErrorCode.DIRECTION);
        }

        if (errorCodes.size() > 0)
        {
            throw new PostException(errorCodes);
        }

        /* Remove any whitespace that may have been accidentally added between commas and then split the CSV at each comma
         */
        tagsList =  Arrays.asList(tags.replaceAll("\\s", "").split(","));

        return new ArrayList<>();
    }
}
