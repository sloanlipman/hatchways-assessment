package com.slipman.assessment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;

@Component
public class HatchawaysAssessmentService
{
    public Ping ping()
    {
        return new Ping(true);
    }

    public List<Post> getPosts(String tags, String sortBy, String direction)
    {
        return new ArrayList<>();
    }
}
