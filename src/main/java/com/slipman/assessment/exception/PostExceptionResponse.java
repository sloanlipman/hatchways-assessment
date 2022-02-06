package com.slipman.assessment.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class PostExceptionResponse {

    @JsonProperty
    private List<String> errors;

    public PostExceptionResponse(PostException postException)
    {
        errors = postException.getErrorCodes().stream().map(PostException.ErrorCode::getCode).collect(Collectors.toList());
    }
}
