package com.challenge.api.service.impl;

import org.springframework.stereotype.Service;

import com.challenge.api.common.dto.ApiRequest;
import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.service.MetricsService;

@Service
public class MetricsServiceImpl implements MetricsService {

	@Override
	public ApiResponse DynamicPercentage(Integer id) {
		Integer sum = id + 5;
		return ApiResponse.success("the result of the operation is:", sum);
	}

}
