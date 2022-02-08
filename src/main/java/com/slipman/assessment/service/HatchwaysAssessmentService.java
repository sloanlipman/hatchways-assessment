package com.slipman.assessment.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
            Arrays.stream(SortAttribute.values()).map(SortAttribute::getAttributeName).collect(Collectors.toList());

    private static final List<String> ALLOWED_SORT_DIRECTIONS =
            Arrays.stream(SortDirection.values()).map(SortDirection::getDirection).collect(Collectors.toList());

    @Autowired
    private GetPostsTaskManager taskManager;

    /**
     * Checks that the server is up and running.
     *
     * @return a successful ping to the server
     */
    public Ping ping()
    {
        return new Ping();
    }

    /**
     * Get a {@link List} of {@link Post}s from Hatchways that correspond to a csv list of tags, sorted by a certain
     * attribute and in a certain direction. If no sort attribute is given, default to {@link Post#getId} (by using
     * {@link SortAttribute#ID}. If no sort direction is given, default to {@link SortDirection#ASCENDING}.
     *
     * @param tags a single tag or a csv list of tags to search by.
     * @param sortBy the attribute by which the data should be ordered
     * @param direction the direction in which the data should be ordered, based on the sortBy
     * @return a list of posts, sorted in the specified direction by the specified attribute
     */
    public List<Post> getPosts(String tags, String sortBy, String direction)
    {
        List<String> tagsList;
        if (StringUtils.isEmpty(tags))
        {
            throw PostException.getMissingTagException();
        }

        SortAttribute sortAttribute = StringUtils.isEmpty(sortBy) ? SortAttribute.ID : SortAttribute.fromString(sortBy);
        SortDirection sortDirection =
                StringUtils.isEmpty(direction) ? SortDirection.ASCENDING : SortDirection.fromString(direction);

        /*
         * Remove any whitespace that may have been accidentally added between commas and then split the CSV at each
         * comma
         */
        tagsList = Arrays.asList(tags.replaceAll("\\s", "").split(","));
        Set<Post> posts = taskManager.getPosts(tagsList);
        List<Post> postsList = new ArrayList<>(posts);

        Comparator<Post> comparator = getComparator(sortAttribute, sortDirection);
        return postsList.stream().sorted(comparator).collect(Collectors.toList());
    }

    Comparator<Post> getComparator(SortAttribute sortAttribute, SortDirection sortDirection)
    {
        Comparator<Post> comparator;
        switch (sortAttribute)
        {
            case LIKES:
                comparator = Comparator.comparing(Post::getLikes);
                break;
            case POPULARITY:
                comparator = Comparator.comparing(Post::getPopularity);
                break;
            case READS:
                comparator = Comparator.comparing(Post::getReads);
                break;
            case ID:
            default:
                comparator = Comparator.comparing(Post::getId);
        }

        if (SortDirection.DESCENDING.equals(sortDirection))
        {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
