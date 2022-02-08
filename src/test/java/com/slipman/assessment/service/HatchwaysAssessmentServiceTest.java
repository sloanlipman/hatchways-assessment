package com.slipman.assessment.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
import com.slipman.assessment.domain.SortAttribute;
import com.slipman.assessment.domain.SortDirection;
import com.slipman.assessment.exception.PostException;

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
        List<String> expectedTagsList = Arrays.asList("health", "science");
        Mockito.doReturn(Comparator.comparing(Post::getId)).when(service)
                .getComparator(ArgumentMatchers.any(SortAttribute.class), ArgumentMatchers.any(SortDirection.class));

        service.getPosts("health,science", null, null);

        Mockito.verify(taskManager).getPosts(ArgumentMatchers.eq(expectedTagsList));
    }
}
