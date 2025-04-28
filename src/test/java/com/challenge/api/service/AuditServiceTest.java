package com.challenge.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.model.ApiAuditLog;
import com.challenge.api.repository.ApiAuditLogRepository;
import com.challenge.api.service.impl.ApiAuditServiceImpl;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ApiAuditServiceImplTest {

    @Mock
    private ApiAuditLogRepository logRepository;

    @InjectMocks
    private ApiAuditServiceImpl apiAuditService;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("should retrieve audit history successfully")
    void testGetAllHistory_Success() throws Exception {

        List<ApiAuditLog> logs = List.of(new ApiAuditLog(), new ApiAuditLog());
        Page<ApiAuditLog> page = new PageImpl<>(logs);

        when(logRepository.findAll(pageable)).thenReturn(page);

        CompletableFuture<ApiResponse<Page<ApiAuditLog>>> futureResponse = apiAuditService.getAllHistory(pageable);
        ApiResponse<Page<ApiAuditLog>> response = futureResponse.get();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getContent()).hasSize(2);
        assertThat(response.getMessage()).isEqualTo("historical logs retrieve");

        verify(logRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("should handle exception when retrieving audit history")
    void testGetAllHistory_Exception() throws Exception {
        
        when(logRepository.findAll(pageable)).thenThrow(new RuntimeException("DB error"));

        CompletableFuture<ApiResponse<Page<ApiAuditLog>>> futureResponse = apiAuditService.getAllHistory(pageable);
        ApiResponse<Page<ApiAuditLog>> response = futureResponse.get();

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("an exception occurs");

        verify(logRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("should save audit log")
    void testSaveLog() {
    	
        ApiAuditLog log = new ApiAuditLog();

        apiAuditService.saveLog(log);

        verify(logRepository, times(1)).save(log);
    }
}
