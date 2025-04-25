package com.challenge.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.service.MetricsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/metrics")
public class MetricsController {
	
	@Autowired
	private MetricsService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getDynamicPercentage(@PathVariable Integer id) {
		log.debug("getProductById");
		ApiResponse response = service.DynamicPercentage(id);
		return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
	}

}										
