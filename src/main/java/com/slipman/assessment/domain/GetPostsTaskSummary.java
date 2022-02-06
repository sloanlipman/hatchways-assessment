package com.slipman.assessment.domain;

import java.util.List;
import java.util.Objects;

public class GetPostsTaskSummary
{
    private final String tag;

    private final List<Post> posts;

    public String getTag()
    {
        return tag;
    }

    public List<Post> getPosts()
    {
        return posts;
    }

    public GetPostsTaskSummary(String tag, List<Post> posts)
    {
        this.tag = tag;
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        GetPostsTaskSummary taskSummary = (GetPostsTaskSummary) o;
        return Objects.equals(tag, taskSummary.tag) && Objects.equals(posts, taskSummary.posts);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tag, posts);
    }
}
