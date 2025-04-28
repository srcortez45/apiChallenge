package com.challenge.api.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.model.ApiAuditLog;
import com.challenge.api.repository.ApiAuditLogRepository;
import com.challenge.api.service.ApiAuditService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiAuditServiceImpl implements ApiAuditService {
	
	@Autowired
	private ApiAuditLogRepository logRepository;

	@Override
	@Async
	public CompletableFuture<ApiResponse<Page<ApiAuditLog>>> getAllHistory(Pageable paging) throws InterruptedException {
		try {
			Page<ApiAuditLog> logs = logRepository.findAll(paging);
			return CompletableFuture.completedFuture(ApiResponse.success("historical logs retrieve", logs));
		}
		catch(Exception e) {
			return CompletableFuture.completedFuture(ApiResponse.failure("an exception occurs"));
		}
		
	}

	@Override
	public void saveLog(ApiAuditLog log) {
		logRepository.save(log);
	}

}
