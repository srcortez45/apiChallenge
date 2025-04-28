package com.challenge.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.challenge.api.service.ExternalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalServiceImpl implements ExternalService {
	
	@Autowired
    private CacheManager cacheManager;

	@Autowired
    private RestTemplate restTemplate;
    
    @Value("${external.service.url}") 
    private String externalServiceUrl;

    
    @Cacheable(value="metrics", unless = "#result == null")
    @Override
    public Integer getMetricNumber() {
    	return fetchMetricFromExternalService();
    }

    @Scheduled(fixedRateString = "${caching.spring.metricsTTL}")
    @Override
    public void emptyMetricsCache() {
        Cache metricsCache = cacheManager.getCache("metrics");

        if (metricsCache == null) {
            log.warn("Metrics cache not found!");
            return;
        }

        Integer newMetric = fetchMetricFromExternalService();
        
        log.info("metric {}", String.valueOf(newMetric));
        if (newMetric != null) {
            log.info("refreshing: updating 'metrics' cache with new value {}", newMetric);
            metricsCache.put(SimpleKey.EMPTY, newMetric);
        } else {
            log.warn("external service unavailable. Keeping previous cached value.");
        }
    }
    
    private Integer fetchMetricFromExternalService() {
        try {
            Integer metricNumber = restTemplate.getForObject(externalServiceUrl, Integer.class);
            log.info("fetched number from external service: {}", metricNumber);
            return metricNumber;
        } catch (Exception e) {
            log.error("error fetching from external service", e);
            Cache metricsCache = cacheManager.getCache("metrics");
            if (metricsCache != null) {
                Integer cachedMetric = metricsCache.get("metrics", Integer.class);
                if (cachedMetric != null) {
                    log.warn("using cached metric value: {}", cachedMetric);
                    return cachedMetric;
                }
            }
            
            log.error("no cached value available for metrics");
            return null;
        }
    }
    
}