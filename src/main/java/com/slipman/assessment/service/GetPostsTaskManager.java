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
import java.util.concurrent.atomic.AtomicLong;
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
     * TODO fill this in
     */
    private static final int THREAD_POOL_SIZE = 1;

    /**
     *
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
     *
     * @param tags
     * @return
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
     *
     * @param tasks
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

    class GetPostsThreadFactory implements ThreadFactory
    {
        private final AtomicLong threadCounter = new AtomicLong(1);

        GetPostsThreadFactory()
        {
            super();
        }

        @Override
        public Thread newThread(Runnable r)
        {
            Thread thread = new Thread(r);
            thread.setName("get-posts-thread-" + threadCounter.getAndIncrement());
            return thread;
        }
    }
}
