package com.challenge.api.service;

import com.challenge.api.common.dto.ApiResponse;


public interface MetricsService {
	
	ApiResponse<Double> DynamicPercentage(Integer num1, Integer num2);

}