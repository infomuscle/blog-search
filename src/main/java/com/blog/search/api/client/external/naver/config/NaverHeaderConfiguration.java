package com.blog.search.api.client.external.naver.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverHeaderConfiguration {

    @Value("${external.api.naver.client-id}")
    private String clientId;

    @Value("${external.api.naver.client-secret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Naver-Client-Id", clientId);
            requestTemplate.header("X-Naver-Client-Secret", clientSecret);
        };
    }
}
