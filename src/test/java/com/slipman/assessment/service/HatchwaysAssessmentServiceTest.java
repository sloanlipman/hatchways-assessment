package com.slipman.assessment.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.exception.PostException;
import com.slipman.assessment.util.DataReader;

@RunWith(MockitoJUnitRunner.class)
public class HatchwaysAssessmentServiceTest
{
    @Spy
    @InjectMocks
    private HatchwaysAssessmentService service;

    @Mock
    private GetPostsTaskManager taskManager;

    @Test
    public void testPing_ExpectSuccessfulPingReturned()
    {
        Assert.assertEquals(new Ping(), service.ping());
    }

    @Test
    public void testGetPosts_TagsAreEmpty_ExpectExceptionThrown()
    {
        try
        {
            service.getPosts("", null, null);
        }
        catch (Exception e)
        {
            Assert.assertEquals(PostException.getMissingTagException(), e);
        }
    }

    @Test
    public void testGetPosts_SortByInvalid_ExpectExceptionThrown()
    {
        try
        {
            service.getPosts("tech", "author", null);
        }
        catch (Exception e)
        {
            Assert.assertEquals(PostException.getInvalidSortByException(), e);
        }
    }

    @Test
    public void testGetPosts_SortDirectionInvalid_ExpectExceptionThrown()
    {
        try
        {
            service.getPosts("tech", "id", "upDown");
        }
        catch (Exception e)
        {
            Assert.assertEquals(PostException.getInvalidDirectionException(), e);
        }
    }

    @Test
    public void testGetPosts_NoSortBy_NoDirection_ExpectSortById_ExpectSortByAscending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", null, null);

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_defaultSort.json"), actualPosts);
    }

    @Test
    public void testGetPosts_SortByLikes_SortAscending_ExpectSortByLikes_ExpectSortByAscending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", "likes", "asc");

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_likes_ascending.json"), actualPosts);
    }

    @Test
    public void testGetPosts_SortByLikes_SortDescending_ExpectSortByLikes_ExpectSortByDescending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", "likes", "desc");

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_likes_descending.json"), actualPosts);
    }

    @Test
    public void testGetPosts_SortByPopularity_SortDescending_ExpectSortByPopularity_ExpectSortByDescending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", "popularity", "desc");

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_popularity_descending.json"), actualPosts);
    }

    @Test
    public void testGetPosts_SortByReads_NoDirection_ExpectSortByReads_ExpectSortByAscending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", "reads", null);

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_reads_ascending.json"), actualPosts);
    }

    @Test
    public void testGetPosts_SortById_SortDescending_ExpectSortById_ExpectSortByDescending_ExpectPostsReturnedSorted()
    {
        List<String> expectedTagsList = Collections.singletonList("tech");
        ;
        Set<Post> postsSet = new HashSet<>(DataReader.readPostFromJson("tech.json"));
        Mockito.doReturn(postsSet).when(taskManager).getPosts(expectedTagsList);

        List<Post> actualPosts = service.getPosts("tech", "id", "desc");

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
        Assert.assertEquals(DataReader.readPostFromJson("tech_id_descending.json"), actualPosts);
    }
}
