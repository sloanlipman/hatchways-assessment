package com.slipman.assessment.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.PostConstant;

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

    /**
     * Reads a JSON file from the classpath (test/resources), converts it to a {@link Map}, extracts the content mapped
     * to "posts", and converts this content to a {@link List} of {@link Post}s
     *
     * @param path the path of the file to be read
     * @return a list of Posts that was contained in the JSON file
     */
    public static List<Post> getPostsListFromJson(String path)
    {
        return getPostsMapFromJson(path).get(PostConstant.POSTS.getValue());
    }

    public static Map<String, List<Post>> getPostsMapFromJson(String path)
    {
        Map<String, List<Map<String, Object>>> fileAsMap = readFile(path);
        Map<String, List<Post>> postsMap = new HashMap<>();
        try
        {
            postsMap = (Map<String, List<Post>>) OBJECT_MAPPER.convertValue(fileAsMap,
                    new TypeReference<Map<String, List<Post>>>()
                    {
                    });
        }
        catch (IllegalArgumentException e)
        {
            Assert.fail(
                    "An exception occurred while trying to convert file to Map<String, List<Post>> " + path + ": " + e);
        }
        return postsMap;
    }

    private static Map<String, List<Map<String, Object>>> readFile(String path)
    {
        ClassPathResource classPathResource = new ClassPathResource("/" + path);
        Map<String, List<Map<String, Object>>> fileAsMap = new HashMap<>();
        try (InputStream input = classPathResource.getInputStream())
        {
            String fileAsJson = IOUtils.toString(input, Charset.defaultCharset());
            fileAsMap = (Map<String, List<Map<String, Object>>>) OBJECT_MAPPER.readValue(fileAsJson, Map.class);
        }
        catch (IOException e)
        {
            Assert.fail("An exception occurred while trying to read file " + path + ": " + e);
        }
        return fileAsMap;
    }
}
