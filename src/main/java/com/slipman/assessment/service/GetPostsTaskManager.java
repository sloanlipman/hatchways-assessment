package com.slipman.assessment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.slipman.assessment.domain.GetPostsTaskSummary;
import com.slipman.assessment.domain.Post;

@Component
public class GetPostsTaskManager
{
    /**
     * The maximum number of requests to make per thread. Assuming that it's likely that a user will only have a handful
     * of tags on each request, set this value to 1 so that every tag is queried in parallel.
     */
    private static final int THREAD_POOL_SIZE = 1;

    /**
     * Holds a record of tags that we've already looked up mapped to the posts that came back.
     */
    private final Map<String, List<Post>> tagToPosts = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    private ExecutorService executorService;

    /**
     * Set up threads and executors at the start of component lifecycle
     */
    @PostConstruct
    void init()
    {
        ThreadFactory delayedPayloadProcessThreadFactory = new GetPostsThreadFactory();
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE, delayedPayloadProcessThreadFactory);
    }

    /**
     * Shut down executors at end of component lifecycle
     */
    @PreDestroy
    private void cleanup()
    {
        executorService.shutdown();
        // Wait for threads to complete
        while (true)
        {
            if (executorService.isTerminated())
            {
                break;
            }
        }
    }

    /**
     * Sets up a list of {@link Future} tasks to be fired off to fetch all relevant {@link Post}s. Once the parallel
     * requests are done, check the cached results and return only the unique Posts that match the requested tags. If
     * the Posts for a tag have already been looked up, defer to the cached list of Posts instead of fetching them
     * again.
     *
     * @param tags a list of input tags to search for
     * @return the unique set of posts that match the requested tags
     */
    Set<Post> getPosts(List<String> tags)
    {
        // Copy the original list of tags so we can use it later
        List<String> tagsToCheck = new ArrayList<>(tags);
        List<Future<?>> tasksList = new ArrayList<>();
        // Only check for tags that we haven't looked up yet
        tags = tags.stream().filter(post -> !tagToPosts.containsKey(post)).collect(Collectors.toList());
        for (String post : tags)
        {
            GetPostsTask task = new GetPostsTask(restTemplate, post);
            tasksList.add(executorService.submit(task));
        }

        fetchResults(tasksList);
        //@formatter:off
        return tagToPosts.keySet().stream()
                .filter(tagsToCheck::contains)
                .map(tagToPosts::get)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        //@formatter:on
    }

    /**
     * Invoke every pending {@link GetPostsTask} and cache the results by mapping the tag to the retrieved
     * {@link Post}s.
     *
     * @param tasks the pending requests to fetch Posts from Hatchways
     */
    private void fetchResults(List<Future<?>> tasks)
    {
        final AtomicInteger counter = new AtomicInteger((1));
        tasks.forEach(task -> {
            try
            {
                GetPostsTaskSummary getPostsTaskSummary = (GetPostsTaskSummary) task.get();
                tagToPosts.put(getPostsTaskSummary.getTag(), getPostsTaskSummary.getPosts());
            }
            catch (InterruptedException | ExecutionException e)
            {
                // Something went wrong. Stop trying.
                Thread.currentThread().interrupt();
            }
        });
    }

    static class GetPostsThreadFactory implements ThreadFactory
    {
        GetPostsThreadFactory()
        {
            super();
        }

        @Override
        public Thread newThread(Runnable r)
        {
            return new Thread(r);
        }
    }
}
