package com.challenge.api.controller;

import com.challenge.api.common.dto.ApiResponse;
import com.challenge.api.model.ApiAuditLog;
import com.challenge.api.service.ApiAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ApiAuditControllerTest {

    @Mock
    private ApiAuditService apiAuditService;

    @InjectMocks
    private ApiAuditController apiAuditController;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should return historical logs successfully")
    void testGetAllHistory_Success() throws Exception {
  
        List<ApiAuditLog> logs = List.of(new ApiAuditLog(), new ApiAuditLog());
        Page<ApiAuditLog> page = new PageImpl<>(logs);
        ApiResponse<Page<ApiAuditLog>> apiResponse = ApiResponse.success("historical logs retrieve", page);

        when(apiAuditService.getAllHistory(pageable)).thenReturn(CompletableFuture.completedFuture(apiResponse));
        ResponseEntity<ApiResponse<Page<ApiAuditLog>>> responseEntity = apiAuditController.getAllHistory(pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(2, responseEntity.getBody().getData().getContent().size());
        assertEquals("historical logs retrieve", responseEntity.getBody().getMessage());

        verify(apiAuditService, times(1)).getAllHistory(pageable);
    }

    @Test
    @DisplayName("Should return failure when historical logs retrieval fails")
    void testGetAllHistory_Failure() throws Exception {

        ApiResponse<Page<ApiAuditLog>> apiResponse = ApiResponse.failure("an exception occurs");

        when(apiAuditService.getAllHistory(pageable)).thenReturn(CompletableFuture.completedFuture(apiResponse));

        ResponseEntity<ApiResponse<Page<ApiAuditLog>>> responseEntity = apiAuditController.getAllHistory(pageable);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isSuccess());
        assertEquals("an exception occurs", responseEntity.getBody().getMessage());

        verify(apiAuditService, times(1)).getAllHistory(pageable);
    }
}