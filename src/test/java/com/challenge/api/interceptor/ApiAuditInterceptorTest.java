package com.challenge.api.interceptor;

import com.challenge.api.service.ApiAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;

import static org.mockito.Mockito.*;

public class ApiAuditInterceptorTest {

    @InjectMocks
    private ApiAuditInterceptor apiAuditInterceptor;

    @Mock
    private ApiAuditService apiAuditService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    @DisplayName("should capture and save audit log")
    void testAuditInterceptorCapturesAndSavesLog() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/v1/metrics");
        request.addHeader("User-Agent", "insomnia/11.0.2");
        request.addParameter("num1", "10");
        request.addParameter("num2", "20");

        response.setStatus(200);

        apiAuditInterceptor.doFilterInternal(request, response, filterChain);

        verify(apiAuditService, times(1)).saveLog(argThat(log ->
                "GET".equals(log.getRequestMethod()) &&
                log.getRequestUrl() != null &&
                log.getRequestParameters().contains("num1") &&
                log.getRequestHeaders().contains("User-Agent") &&
                log.getResponseStatus() == 200
        ));
    }
}
