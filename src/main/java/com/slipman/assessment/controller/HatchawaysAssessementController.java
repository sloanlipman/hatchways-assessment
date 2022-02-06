package com.slipman.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slipman.assessment.exception.PostExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.domain.Post;
import com.slipman.assessment.exception.PostException;
import com.slipman.assessment.service.HatchawaysAssessmentService;

@RestController
@RequestMapping("/api")
@ControllerAdvice
public class HatchawaysAssessementController
{
    @Autowired
    private HatchawaysAssessmentService service;

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
        response.put("posts", posts);
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
