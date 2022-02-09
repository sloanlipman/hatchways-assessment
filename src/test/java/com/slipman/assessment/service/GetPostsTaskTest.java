package com.slipman.assessment.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.slipman.assessment.domain.GetPostsTaskSummary;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.PostConstant;
import com.slipman.assessment.util.DataReader;

@RunWith(MockitoJUnitRunner.class)
public class GetPostsTaskTest
{
    private final ParameterizedTypeReference<Map<String, List<Post>>> PARAMETERIZED_TYPE_REFERENCE =
            new ParameterizedTypeReference<Map<String, List<Post>>>()
            {
            };

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private GetPostsTask getPostsTask;

    @Before
    public void setUp()
    {
        getPostsTask = new GetPostsTask(restTemplate, "tech");
    }

    @Test
    public void testCall_ResponseBodyIsNull_ExpectEmptyArrayListReturned()
    {
        Mockito.doReturn(ResponseEntity.ok(null)).when(restTemplate).exchange(ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HttpMethod.GET), ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(PARAMETERIZED_TYPE_REFERENCE));

        GetPostsTaskSummary actualSummary = getPostsTask.call();
        GetPostsTaskSummary expectedSummary = new GetPostsTaskSummary("tech", new ArrayList<>());
        Assert.assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testCall_ResponseBodyHasPosts_ExpectListOfPostsReturned()
    {
        Map<String, List<Post>> posts = DataReader.getPostsMapFromJson("tech.json");
        Mockito.doReturn(ResponseEntity.ok(posts)).when(restTemplate).exchange(ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HttpMethod.GET), ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(PARAMETERIZED_TYPE_REFERENCE));

        GetPostsTaskSummary actualSummary = getPostsTask.call();
        GetPostsTaskSummary expectedSummary = new GetPostsTaskSummary("tech", posts.get(PostConstant.POSTS.getValue()));
        Assert.assertEquals(expectedSummary, actualSummary);
    }
}
