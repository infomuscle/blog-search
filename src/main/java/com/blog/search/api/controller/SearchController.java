package com.blog.search.api.controller;

import com.blog.search.api.client.dto.ClientSearchResponse;
import com.blog.search.api.dto.SearchResponse;
import com.blog.search.api.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/blog/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 좀 더 고민 필요
    // api 명시 여부
    // 검색어 = 자원? -> blog/query/{query}?

    @GetMapping
    public SearchResponse search(@RequestParam String query, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        // sort = accuracy, recency

        return searchService.search(query, sort, page, size);
    }

    @GetMapping("/top/{size}")
    public String listTopQueries(@RequestParam String query, @PathVariable Integer size) {
        log.info("SearchController.listTopQueries# query: {} ", query);

        return query;
    }

}
