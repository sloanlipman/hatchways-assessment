package com.slipman.assessment.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import com.slipman.assessment.domain.Post;

public class DataReader
{
    // Make this class non-instantiable
    private DataReader()
    {
    }

    /**
     * Creating an instance of {@link ObjectMapper} is expensive. Maintain a single instance.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<Post> readPostFromJson(String path)
    {
        ClassPathResource classPathResource = new ClassPathResource("/" + path);
        List<Post> posts = null;
        Map<String, List<Map<String, Object>>> postsMap;
        try (InputStream input = classPathResource.getInputStream())
        {
            String postAsJson = IOUtils.toString(input, Charset.defaultCharset());
            postsMap = (Map<String, List<Map<String, Object>>>) OBJECT_MAPPER.readValue(postAsJson, Map.class);
            List<Map<String, Object>> postsList = postsMap.get("posts");
            posts = (List<Post>) OBJECT_MAPPER.convertValue(postsList, new TypeReference<List<Post>>()
            {
            });
        }
        catch (IOException e)
        {
            Assert.fail("An exception occurred while trying to read file " + path + ": " + e);
        }
        return posts;
    }
}
