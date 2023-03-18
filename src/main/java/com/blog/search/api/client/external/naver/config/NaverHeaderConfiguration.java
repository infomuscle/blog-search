package com.blog.search.api.client.external.naver.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverHeaderConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Naver-Client-Id", "CczUhGiv65FMDTV1YzoH");
            requestTemplate.header("X-Naver-Client-Secret", "GLpUOLYiPn");
        };
    }
}
