package com.slipman.assessment.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.SortAttribute;
import com.slipman.assessment.domain.SortDirection;
import com.slipman.assessment.exception.PostException;

@Component
public class HatchwaysAssessmentService
{
    private static final List<String> ALLOWED_SORT_ATTRIBUTES =
            Arrays.stream(SortAttribute.values()).map(SortAttribute::getName).collect(Collectors.toList());

    private static final List<String> ALLOWED_SORT_DIRECTIONS =
            Arrays.stream(SortDirection.values()).map(SortDirection::getDirection).collect(Collectors.toList());

    @Autowired
    private GetPostsTaskManager taskManager;

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
    public Set<Post> getPosts(String tags, String sortBy, String direction)
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

        SortDirection sortDirection;
        /*
         * If there was no specified direction, default to ascending. Otherwise, use the specified value or throw an
         * error if the specified value isn't valid.
         */
        if (StringUtils.isEmpty(direction))
        {
            sortDirection = SortDirection.ASCENDING;
        }
        else if (ALLOWED_SORT_DIRECTIONS.contains((direction)))
        {
            sortDirection = SortDirection.valueOf(direction);
        }
        else
        {
            errorCodes.add(PostException.ErrorCode.DIRECTION);
        }

        if (errorCodes.size() > 0)
        {
            throw new PostException(errorCodes);
        }

        /*
         * Remove any whitespace that may have been accidentally added between commas and then split the CSV at each
         * comma
         */
        tagsList = Arrays.asList(tags.replaceAll("\\s", "").split(","));
        Set<Post> posts = taskManager.getPosts(tagsList);
        // then do sorting
        return posts;
    }
}
