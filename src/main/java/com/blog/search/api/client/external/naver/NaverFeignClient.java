package com.blog.search.api.client.external.naver;

import com.blog.search.api.client.external.naver.config.NaverHeaderConfiguration;
import com.blog.search.api.client.external.naver.message.NaverSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverFeignClient", url = "${external.api.naver.url}", configuration = NaverHeaderConfiguration.class)
public interface NaverFeignClient {

    @GetMapping(value = "/v1/search/blog.json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    NaverSearchResponse search(@RequestParam String query, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer start, @RequestParam(required = false) Integer display);

}
