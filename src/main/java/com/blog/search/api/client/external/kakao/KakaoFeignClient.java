package com.blog.search.api.client.external.kakao;

import com.blog.search.api.client.external.kakao.message.KakaoSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoFeignClient", url = "${external.api.kakao.url}")
public interface KakaoFeignClient {

    @GetMapping(value = "/v2/search/blog", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Authorization=KakaoAK 835e5b753f07038006456b0eff431ec6")
    KakaoSearchResponse search(@RequestParam String query, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size);

}
