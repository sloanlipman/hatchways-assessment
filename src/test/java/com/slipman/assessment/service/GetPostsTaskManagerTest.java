package com.slipman.assessment.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.slipman.assessment.domain.Post;
import com.slipman.assessment.util.DataReader;

@RunWith(MockitoJUnitRunner.class)
public class GetPostsTaskManagerTest
{
    @Spy
    @InjectMocks
    private GetPostsTaskManager taskManager;

    @Mock
    private ExecutorService executorService;

    private final List<Post> HEALTH_POSTS = DataReader.getPostsListFromJson("health.json");

    private final List<Post> SCIENCE_POSTS = DataReader.getPostsListFromJson("science.json");

    private final List<Post> TECH_POSTS = DataReader.getPostsListFromJson("tech.json");

    @After
    public void cleanUp()
    {
        taskManager.getCachedTagsToPosts().clear();
    }

    @Test
    public void testGetPosts_CacheIsEmpty_ExpectAllInputTagsFetched()
    {
        Mockito.doNothing().when(taskManager).createAndSubmitTask(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyList());

        Map<String, List<Post>> fetchedPosts = new HashMap<>();
        fetchedPosts.put("health", HEALTH_POSTS);
        fetchedPosts.put("science", SCIENCE_POSTS);
        fetchedPosts.put("tech", TECH_POSTS);
        Mockito.doReturn(fetchedPosts).when(taskManager).fetchResults(ArgumentMatchers.anyList());

        List<String> tags = Arrays.asList("tech", "health", "science");
        Set<Post> posts = taskManager.getPosts(tags);

        Set<Post> expectedPosts = new HashSet<>();
        expectedPosts.addAll(HEALTH_POSTS);
        expectedPosts.addAll(SCIENCE_POSTS);
        expectedPosts.addAll(TECH_POSTS);

        Assert.assertEquals(expectedPosts, posts);
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("tech"), ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("health"), ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("science"), ArgumentMatchers.anyList());
    }

    @Test
    public void testGetPosts_CacheHasARequestedTag_ExpectOnlyNewTagsFetched()
    {
        Mockito.doNothing().when(taskManager).createAndSubmitTask(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyList());

        Map<String, List<Post>> fetchedPosts = new HashMap<>();
        fetchedPosts.put("health", HEALTH_POSTS);
        fetchedPosts.put("science", SCIENCE_POSTS);
        Mockito.doReturn(fetchedPosts).when(taskManager).fetchResults(ArgumentMatchers.anyList());

        taskManager.getCachedTagsToPosts().put("tech", TECH_POSTS);

        List<String> tags = Arrays.asList("tech", "health", "science");
        Set<Post> posts = taskManager.getPosts(tags);

        Set<Post> expectedPosts = new HashSet<>();
        expectedPosts.addAll(HEALTH_POSTS);
        expectedPosts.addAll(SCIENCE_POSTS);
        expectedPosts.addAll(TECH_POSTS);

        Assert.assertEquals(expectedPosts, posts);
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("tech"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("health"), ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("science"), ArgumentMatchers.anyList());
    }

    @Test
    public void testGetPosts_CacheHasANonRequestedTag_ExpectOnlyNewTagsFetched_ExpectOnlyRequestedTagsReturned()
    {
        Mockito.doNothing().when(taskManager).createAndSubmitTask(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyList());

        Map<String, List<Post>> fetchedPosts = new HashMap<>();
        fetchedPosts.put("health", HEALTH_POSTS);
        fetchedPosts.put("science", SCIENCE_POSTS);
        Mockito.doReturn(fetchedPosts).when(taskManager).fetchResults(ArgumentMatchers.anyList());

        taskManager.getCachedTagsToPosts().put("tech", TECH_POSTS);

        List<String> tags = Arrays.asList("health", "science");
        Set<Post> posts = taskManager.getPosts(tags);

        Set<Post> expectedPosts = new HashSet<>();
        expectedPosts.addAll(HEALTH_POSTS);
        expectedPosts.addAll(SCIENCE_POSTS);

        Assert.assertEquals(expectedPosts, posts);
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("tech"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("health"), ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("science"), ArgumentMatchers.anyList());
    }

    @Test
    public void testGetPosts_CacheHasANonRequestedTag_CacheHasARequestedTag_ExpectOnlyNewTagsFetched_ExpectOnlyRequestedTagsReturned()
    {
        Mockito.doNothing().when(taskManager).createAndSubmitTask(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyList());

        Map<String, List<Post>> fetchedPosts = new HashMap<>();
        fetchedPosts.put("science", SCIENCE_POSTS);
        Mockito.doReturn(fetchedPosts).when(taskManager).fetchResults(ArgumentMatchers.anyList());

        taskManager.getCachedTagsToPosts().put("health", HEALTH_POSTS);
        taskManager.getCachedTagsToPosts().put("tech", TECH_POSTS);

        List<String> tags = Arrays.asList("health", "science");
        Set<Post> posts = taskManager.getPosts(tags);

        Set<Post> expectedPosts = new HashSet<>();
        expectedPosts.addAll(HEALTH_POSTS);
        expectedPosts.addAll(SCIENCE_POSTS);

        Assert.assertEquals(expectedPosts, posts);
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("tech"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("health"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager).createAndSubmitTask(ArgumentMatchers.eq("science"), ArgumentMatchers.anyList());
    }

    @Test
    public void testGetPosts_CacheHasAllRequestedTags_ExpectNoNewTagsFetched_ExpectOnlyRequestedTagsReturned()
    {
        taskManager.getCachedTagsToPosts().put("health", HEALTH_POSTS);
        taskManager.getCachedTagsToPosts().put("science", SCIENCE_POSTS);
        taskManager.getCachedTagsToPosts().put("tech", TECH_POSTS);

        List<String> tags = Arrays.asList("health", "science");
        Set<Post> posts = taskManager.getPosts(tags);

        Set<Post> expectedPosts = new HashSet<>();
        expectedPosts.addAll(HEALTH_POSTS);
        expectedPosts.addAll(SCIENCE_POSTS);

        Assert.assertEquals(expectedPosts, posts);
        Mockito.verify(taskManager, Mockito.times(0)).fetchResults(ArgumentMatchers.anyList());
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("tech"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("health"),
                ArgumentMatchers.anyList());
        Mockito.verify(taskManager, Mockito.times(0)).createAndSubmitTask(ArgumentMatchers.eq("science"),
                ArgumentMatchers.anyList());
    }
}
