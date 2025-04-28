package com.challenge.api.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.model.ApiAuditLog;
import com.challenge.api.service.ApiAuditService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/history")
public class ApiAuditController {
	
	@Autowired
	private ApiAuditService logService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<ApiAuditLog>>> getAllHistory(final Pageable paging) throws InterruptedException, ExecutionException {
		log.info("getAllHistory");
		ApiResponse<Page<ApiAuditLog>> responseApi = logService.getAllHistory(paging).get();
	    return ResponseEntity.status(responseApi.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(responseApi);

	}

}