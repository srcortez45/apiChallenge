package com.challenge.api.controller;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.service.MetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricsControllerTest {

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private MetricsController metricsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return OK when service returns success")
    void testGetDynamicPercentageSuccess() throws Exception {
    	
        ApiResponse<Double> expectedResponse = ApiResponse.success("metric calculated", 33.0);
        
        when(metricsService.DynamicPercentage(10,20)).thenReturn(expectedResponse);

        ResponseEntity<ApiResponse<Double>> responseEntity = metricsController.getDynamicPercentage(10, 20);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        
    }

    @Test
    @DisplayName("Should return NOT_FOUND when service returns failure")
    void testGetDynamicPercentageFailure() {

        ApiResponse<Double> expectedResponse = ApiResponse.failure("error calculating metric");
        when(metricsService.DynamicPercentage(10, 20)).thenReturn(expectedResponse);

        ResponseEntity<ApiResponse<Double>> responseEntity = metricsController.getDynamicPercentage(10, 20);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
