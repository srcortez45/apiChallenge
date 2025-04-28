package com.challenge.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.challenge.api.service.impl.ExternalServiceImpl;


@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
@TestPropertySource(properties = "external.service.url=http://localhost:3000/api/random-number")
public class ExternalServiceImplTest {

    @Mock
    private Cache cache;
	
	@Mock
    private CacheManager cacheManager;
	
    @Mock
    private RestTemplate restTemplate;
	
    @InjectMocks
    private ExternalServiceImpl externalServiceImpl;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(externalServiceImpl, "externalServiceUrl", "http://localhost:3000/api/random-number");
    }
    
    
    @Test
    @DisplayName("should go to external service to fetch metric")
    void testExternalServiceSuccessfully() {
    	
    	when(restTemplate.getForObject(Mockito.anyString(),Mockito.eq(Integer.class))).thenReturn(25);

    	 Integer result = externalServiceImpl.getMetricNumber();
    	
    	assertNotNull(result);
        assertEquals(25, result);
      
    }
    
    @Test
    @DisplayName("should fallback to cached value when external service fails")
    void testGetMetricNumber_FallbackToCache() {
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Integer.class)))
            .thenThrow(new RuntimeException("Service Down"));

        when(cacheManager.getCache("metrics")).thenReturn(cache);
        when(cache.get(Mockito.any(), Mockito.eq(Integer.class))).thenReturn(42);

        Integer result = externalServiceImpl.getMetricNumber();

        assertNotNull(result);
        assertEquals(42, result);
    }
    
    @Test
    @DisplayName("should return null when no external service and no cached value available")
    void testGetMetricNumber_NoCacheAvailable() {
    	
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Integer.class)))
            .thenThrow(new RuntimeException("Service Down"));

        when(cacheManager.getCache("metrics")).thenReturn(null); 

        Integer result = externalServiceImpl.getMetricNumber();

        assertEquals(result, null);
    }
    
    @Test
    @DisplayName("should refresh cache with new value")
    void testEmptyMetricsCache_RefreshSuccess() {
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Integer.class))).thenReturn(30);
        when(cacheManager.getCache("metrics")).thenReturn(cache);

        externalServiceImpl.emptyMetricsCache();

        Mockito.verify(cache).put(SimpleKey.EMPTY, 30);
    }
    
    @Test
    @DisplayName("should not refresh cache if external service fails")
    void testEmptyMetricsCache_RefreshFails() {
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Integer.class)))
            .thenThrow(new RuntimeException("Service Down"));

        when(cacheManager.getCache("metrics")).thenReturn(cache);

        externalServiceImpl.emptyMetricsCache();

        Mockito.verify(cache, Mockito.never()).put(Mockito.any(), Mockito.any());
    }
    
    @Test
    @DisplayName("should log warning message for no cache found")
    void testEmptyMetricsCache_WarningNoCache(CapturedOutput output) {
        
        when(cacheManager.getCache("metrics")).thenReturn(null);

        externalServiceImpl.emptyMetricsCache();

        assertThat(output)
        .asString()
        .contains("Metrics cache not found!");
    }

}
