package com.slipman.assessment.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post
{
    @JsonProperty
    private int id;

    @JsonProperty
    private String author;

    @JsonProperty
    private int authorId;

    @JsonProperty
    private int likes;

    @JsonProperty
    private double popularity;

    @JsonProperty
    private int reads;

    @JsonProperty
    private List<String> tags;

    public int getId()
    {
        return id;
    }

    public int getReads()
    {
        return reads;
    }

    public int getLikes()
    {
        return likes;
    }

    public double getPopularity()
    {
        return popularity;
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
        Post post = (Post) o;
        return id == post.id && authorId == post.authorId && likes == post.likes
                && Double.compare(post.popularity, popularity) == 0 && reads == post.reads
                && Objects.equals(author, post.author) && Objects.equals(tags, post.tags);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "Post{" + "id=" + id + ", author='" + author + '\'' + ", authorId=" + authorId + ", likes=" + likes
                + ", popularity=" + popularity + ", reads=" + reads + ", tags=" + tags + '}';
    }
}
