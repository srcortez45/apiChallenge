package com.challenge.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.challenge.api.model.ApiAuditLog;

@Repository
public interface ApiAuditLogRepository extends PagingAndSortingRepository<ApiAuditLog, Long>, CrudRepository<ApiAuditLog, Long> {
	
}