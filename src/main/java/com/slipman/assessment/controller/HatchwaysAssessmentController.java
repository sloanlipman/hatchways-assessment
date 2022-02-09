package com.slipman.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.domain.PostConstant;
import com.slipman.assessment.domain.SortAttribute;
import com.slipman.assessment.domain.SortDirection;
import com.slipman.assessment.exception.PostException;
import com.slipman.assessment.exception.PostExceptionResponse;
import com.slipman.assessment.service.HatchwaysAssessmentService;

@RestController
@RequestMapping("/api")
@ControllerAdvice
public class HatchwaysAssessmentController
{
    @Autowired
    private HatchwaysAssessmentService service;

    /**
     * Checks that the server is up and running.
     *
     * @return a successful ping to the server
     */
    @GetMapping("/ping")
    public ResponseEntity<Ping> ping()
    {
        Ping ping = service.ping();
        return ResponseEntity.ok(ping);
    }

    /**
     * Get a {@link List} of {@link Post}s from Hatchways that correspond to a csv list of tags, sorted by a certain
     * attribute and in a certain direction. If no sort attribute is given, default to {@link Post#getId} (by using
     * {@link SortAttribute#ID}. If no sort direction is given, default to {@link SortDirection#ASCENDING}.
     *
     * @param tags a single tag or a csv list of tags to search by.
     * @param sortBy the attribute by which the data should be ordered
     * @param direction the direction in which the data should be ordered, based on the sortBy
     * @return a list of posts, sorted in the specified direction by the specified attribute
     */
    @GetMapping("/posts")
    public ResponseEntity<Map<String, List<Post>>> getPosts(@RequestParam(value = "tags") String tags,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "direction", required = false) String direction)
    {
        List<Post> posts = service.getPosts(tags, sortBy, direction);
        Map<String, List<Post>> response = new HashMap<>();
        response.put(PostConstant.POSTS.getValue(), posts);
        return ResponseEntity.ok(response);
    }

    /**
     * Catches thrown {@link PostException} errors and returns them in a meaningful way
     *
     * @param postException the thrown exception
     * @return a valid JSON response that shows the error in a meaningful way
     */
    @ExceptionHandler(PostException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostExceptionResponse handleError(PostException postException)
    {
        return new PostExceptionResponse(postException);
    }

    /**
     * Catches errors when the tags parameter is missing from
     * {@link HatchwaysAssessmentController#getPosts(String, String, String)}.
     *
     * @param exception the thrown exception for when a request parameter is missing
     * @return a valid JSON response that shows the error in a meaningful way
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostExceptionResponse handleMissingParameter(MissingServletRequestParameterException exception)
    {
        return new PostExceptionResponse(PostException.getMissingTagException());
    }
}
