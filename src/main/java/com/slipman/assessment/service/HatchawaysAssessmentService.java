package com.slipman.assessment.service;

import org.springframework.stereotype.Component;

import com.slipman.assessment.domain.Ping;

@Component
public class HatchawaysAssessmentService
{
    public Ping ping()
    {
        return new Ping(true);
    }
}
