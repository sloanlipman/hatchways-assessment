package com.slipman.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.PostConstant;
import com.slipman.assessment.service.HatchwaysAssessmentService;
import com.slipman.assessment.util.DataReader;

@RunWith(MockitoJUnitRunner.class)
public class HatchwaysAssessmentControllerTest
{
    @Spy
    @InjectMocks
    private HatchwaysAssessmentController controller;

    @Mock
    HatchwaysAssessmentService service;

    @Test
    public void testPing_ExpectPingReturned()
    {
        Mockito.doReturn(new Ping()).when(service).ping();
        Assert.assertEquals(ResponseEntity.ok(new Ping()), controller.ping());
    }

    @Test
    public void testGetPosts_ExpectPostsReturnedAsMap()
    {
        List<Post> returnedPosts = DataReader.getPostsListFromJson("tech.json");
        Mockito.doReturn(returnedPosts).when(service).getPosts(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        Map<String, List<Post>> expectedResponse = new HashMap<>();
        expectedResponse.put(PostConstant.POSTS.getValue(), returnedPosts);

        Assert.assertEquals(ResponseEntity.ok(expectedResponse), controller.getPosts("tech", "likes", "desc"));
    }
}
