package com.challenge.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.challenge.api.common.dto.ApiResponse;

@ControllerAdvice
public class RequestExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiResponse<Void> response = ApiResponse.failure("Required parameter is missing or invalid.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
