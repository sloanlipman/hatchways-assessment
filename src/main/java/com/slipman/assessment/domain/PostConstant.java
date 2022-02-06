package com.slipman.assessment.domain;

public enum PostConstant
{
    BASE_URL("https://api.hatchways.io/assessment/blog/posts?tag="),
    POSTS("posts");

    private final String value;

    public String getValue()
    {
        return value;
    }

    PostConstant(String value)
    {
        this.value = value;
    }
}
