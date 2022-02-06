package com.slipman.assessment.exception;

public class PostException extends RuntimeException
{
    private final ErrorCode errorCode;

    public static PostException getInvalidDirectionException()
    {
        return new PostException(ErrorCode.DIRECTION);
    }

    public static PostException getInvalidSortByException()
    {
        return new PostException(ErrorCode.SORT_BY);
    }

    public static PostException getMissingTagException()
    {
        return new PostException(ErrorCode.TAGS);
    }

    private PostException(ErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode()
    {
        return errorCode;
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
