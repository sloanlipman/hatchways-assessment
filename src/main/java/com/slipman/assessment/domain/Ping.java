package com.slipman.assessment.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ping
{
    @JsonProperty
    private boolean success;

    public Ping(boolean success)
    {
        this.success = success;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Ping ping = (Ping) o;
        return success == ping.success;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(success);
    }
}
