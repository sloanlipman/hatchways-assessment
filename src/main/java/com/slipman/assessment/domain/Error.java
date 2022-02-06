package com.slipman.assessment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error
{
    @JsonProperty
    private String error;

    enum ErrorCode
    {
        INVALID_DIRECTION("direction parameter is invalid"),
        INVALID_SORT("sortBy parameter is invalid"),
        TAGS_MISSING("Tags parameter is required");

        private final String code;

        public String getCode()
        {
            return code;
        }

        ErrorCode(String code)
        {
            this.code = code;
        }
    }
}
