package com.challenge.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.service.MetricsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MetricsServiceImpl implements MetricsService {
	
	@Autowired
	private ExternalServiceImpl externalService;

	@Override
	public ApiResponse<Double> DynamicPercentage(Integer num1, Integer num2) {
	    Double sum = (double) num1 + (double) num2;
	    log.info("sum Dynamic percentage {}", sum);

	    Integer metricNumber = externalService.getMetricNumber();
	    log.info("metric Dynamic percentage {}", metricNumber);

	    if (metricNumber == null) {
	        return ApiResponse.failure("error calculating metric");
	    }

	    double metricResult = sum * (1 + (metricNumber.doubleValue() / 100));

	    return ApiResponse.success("metric calculated", metricResult);
	}

}
