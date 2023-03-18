package com.blog.search.api.client;

import com.blog.search.api.client.dto.ClientSearchResponse;
import com.blog.search.api.client.external.ExternalSearchResponse;
import com.blog.search.api.client.external.kakao.KakaoFeignClient;
import com.blog.search.api.client.external.naver.NaverFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchClient {

    private final KakaoFeignClient kakaoFeignClient;
    private final NaverFeignClient naverFeignClient;

    public ClientSearchResponse search(String query, String sort, Integer page, Integer size) {
        ExternalSearchResponse externalResponse = kakaoFeignClient.search(query, sort, page, size);
        // ExternalSearchResponse externalResponse = naverFeignClient.search(query, sort, (page - 1) * size + 1, size);

        return ClientSearchResponse.from(externalResponse);
    }
}
