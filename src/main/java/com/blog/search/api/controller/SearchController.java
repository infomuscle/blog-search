package com.blog.search.api.controller;

import com.blog.search.api.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String search(@RequestParam String query, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("SearchController.search# query: {} ", query);
        searchService.search(query, sort, page, size);

        return query;
    }

    @GetMapping("/top")
    public String listTopQueries(@RequestParam String query) {
        log.info("SearchController.listTopQueries# query: {} ", query);

        return query;
    }

}
