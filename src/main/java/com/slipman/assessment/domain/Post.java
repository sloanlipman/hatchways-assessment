package com.slipman.assessment.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post
{
    @JsonProperty
    private int id;

    private String author;

    private int authorId;

    private int likes;

    private double popularity;

    private int reads;

    private List<String> tags;
}
