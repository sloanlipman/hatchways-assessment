package com.slipman.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipman.assessment.domain.Ping;
import com.slipman.assessment.service.HatchawaysAssessmentService;

@RestController
@RequestMapping("/api")
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
}
