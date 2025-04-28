package com.challenge.api.service;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.service.impl.ExternalServiceImpl;
import com.challenge.api.service.impl.MetricsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricsServiceImplTest {

    @Mock
    private ExternalServiceImpl externalService;

    @InjectMocks
    private MetricsServiceImpl metricsService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should calculate dynamic percentage successfully")
    void testDynamicPercentageSuccess() {
    	
    	when(externalService.getMetricNumber()).thenReturn(10);

        ApiResponse<Double> response = metricsService.DynamicPercentage(10, 20);

        assertTrue(response.isSuccess());
        assertEquals("metric calculated", response.getMessage());
        assertNotNull(response.getData());

        Double result = (Double) response.getData();
        assertEquals(33.0, result);
    }

    @Test
    @DisplayName("Should fail when external service return null")
    void testDynamicPercentageExternalServiceFails() {

    	when(externalService.getMetricNumber()).thenReturn(null);
        
        ApiResponse<Double> response = metricsService.DynamicPercentage(10, 20);

        assertFalse(response.isSuccess());
        assertEquals("error calculating metric", response.getMessage());
    }
}

