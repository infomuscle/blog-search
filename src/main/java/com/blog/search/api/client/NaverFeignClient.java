package com.blog.search.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverFeignClient", url = "${external.api.naver.url}")
public interface NaverFeignClient {

    @GetMapping(value = "/v1/search/blog.json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    String search(@RequestParam String query, @RequestParam(required = false) Integer display, @RequestParam(required = false) Integer start, @RequestParam(required = false) String sort);

}
