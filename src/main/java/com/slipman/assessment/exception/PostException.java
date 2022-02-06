package com.slipman.assessment.exception;

import java.util.List;

public class PostException extends RuntimeException
{
    List<ErrorCode> errorCodes;

    public PostException(List<ErrorCode> errorCodes)
    {
        this.errorCodes = errorCodes;
    }

    public List<ErrorCode> getErrorCodes()
    {
        return errorCodes;
    }

    public enum ErrorCode
    {
        DIRECTION("direction parameter is invalid"),
        SORT_BY("sortBy parameter is invalid"),
        TAGS("Tags parameter is required");

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
