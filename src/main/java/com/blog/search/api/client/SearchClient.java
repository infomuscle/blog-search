package com.blog.search.api.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchClient {

    private final KakaoFeignClient kakaoFeignClient;
    private final NaverFeignClient naverFeignClient;

    public String search(String query, String sort, Integer page, Integer size) {

        String response = kakaoFeignClient.search(query, sort, page, size);
        log.info("SearchClient.search# response: {} ", response);

        return response;
    }
}
