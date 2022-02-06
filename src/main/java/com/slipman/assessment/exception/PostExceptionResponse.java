package com.slipman.assessment.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostExceptionResponse
{
    @JsonProperty
    private final String error;

    public PostExceptionResponse(PostException postException)
    {
        error = postException.getErrorCode().getCode();
    }
}
