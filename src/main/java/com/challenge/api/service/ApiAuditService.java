package com.challenge.api.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.model.ApiAuditLog;

public interface ApiAuditService {
	
	CompletableFuture<ApiResponse<Page<ApiAuditLog>>> getAllHistory(Pageable paging) throws InterruptedException;

    void saveLog(ApiAuditLog log);
	
}
