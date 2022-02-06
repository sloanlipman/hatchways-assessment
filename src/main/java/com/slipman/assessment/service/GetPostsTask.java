package com.slipman.assessment.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.slipman.assessment.domain.GetPostsTaskSummary;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.PostConstant;

public class GetPostsTask implements Callable<GetPostsTaskSummary>
{
    private final RestTemplate restTemplate;

    private final String tag;

    GetPostsTask(RestTemplate restTemplate, String tag)
    {
        this.restTemplate = restTemplate;
        this.tag = tag;
    }

    public GetPostsTaskSummary call()
    {
        return new GetPostsTaskSummary(tag, getPosts());
    }

    private List<Post> getPosts()
    {
        URI uri = UriComponentsBuilder.fromUriString(PostConstant.BASE_URL.getValue() + tag).build().encode().toUri();
        ResponseEntity<Map<String, List<Post>>> response = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()), new ParameterizedTypeReference<Map<String, List<Post>>>()
                {
                });
        if (response.getBody() != null)
        {
            return response.getBody().get(PostConstant.POSTS.getValue());
        }
        return new ArrayList<>();
    }

    private HttpHeaders getHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
