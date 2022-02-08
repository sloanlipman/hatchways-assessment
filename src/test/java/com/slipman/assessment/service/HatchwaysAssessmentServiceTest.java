package com.slipman.assessment.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.slipman.assessment.domain.Ping;

@RunWith(MockitoJUnitRunner.class)
public class HatchwaysAssessmentServiceTest
{
    @Spy
    private static HatchwaysAssessmentService service;

    @Test
    public void testPing_ExpectSuccessfulPingReturned()
    {
        Assert.assertEquals(new Ping(), service.ping());
    }
}
