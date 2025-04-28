package com.challenge.api.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.challenge.api.model.ApiAuditLog;
import com.challenge.api.service.ApiAuditService;
import com.challenge.api.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ApiAuditInterceptor extends OncePerRequestFilter {

	@Autowired
	private ApiAuditService apiAuditService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {

			String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

			String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

			ApiAuditLog log = ApiAuditLog.builder().requestMethod(request.getMethod()).requestUrl("url")
					.requestParameters(parametersToJson(request)).requestHeaders(headersToJson(request))
					.requestBody(requestBody).responseStatus(response.getStatus())
					.responseHeaders(headersToJson(response)).responseBody(responseBody).build();

			apiAuditService.saveLog(log);

			responseWrapper.copyBodyToResponse();
		}
	}

	private String parametersToJson(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames != null && parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			params.put(paramName, request.getParameter(paramName));
		}

		return JsonUtils.toJson(params);
	}

	private String headersToJson(HttpServletRequest request) {
		Map<String, String> headers = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String header = headerNames.nextElement();
			headers.put(header, request.getHeader(header));
		}
		return JsonUtils.toJson(headers);
	}

	private String headersToJson(HttpServletResponse response) {
		Map<String, String> headers = new HashMap<>();
		for (String headerName : response.getHeaderNames()) {
			headers.put(headerName, response.getHeader(headerName));
		}
		return JsonUtils.toJson(headers);
	}
}
