package com.slipman.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.slipman.assessment.exception.PostException;
import com.slipman.assessment.exception.PostExceptionResponse;
import com.slipman.assessment.service.HatchwaysAssessmentService;

@RestController
@RequestMapping("/api")
@ControllerAdvice
public class HatchwaysAssessementController
{
    @Autowired
    private HatchwaysAssessmentService service;

    @GetMapping("/ping")
    public ResponseEntity<Ping> ping()
    {
        Ping ping = service.ping();
        return ResponseEntity.ok(ping);
    }

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

    @ExceptionHandler(PostException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostExceptionResponse handleError(PostException postException)
    {
        return new PostExceptionResponse(postException);
    }
}
