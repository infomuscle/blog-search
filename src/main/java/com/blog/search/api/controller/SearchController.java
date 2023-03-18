package com.blog.search.api.controller;

import com.blog.search.api.dto.ApiResponse;
import com.blog.search.api.dto.SearchResponse;
import com.blog.search.api.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/blog/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private static final String INVALID_PAGE_MESSAGE = "페이지는 1~50 범위까지 지원합니다.";
    private static final String INVALID_SIZE_MESSAGE = "페이지 크기는 1~50 범위까지 지원합니다.";

    private final SearchService searchService;

    // 좀 더 고민 필요
    // api 명시 여부
    // 검색어 = 자원? -> blog/query/{query}?

    // sort = accuracy, recency

    @GetMapping
    public ApiResponse search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "accuracy") String sort,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = INVALID_PAGE_MESSAGE) @Max(value = 50, message = INVALID_PAGE_MESSAGE) Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1, message = INVALID_SIZE_MESSAGE) @Max(value = 50, message = INVALID_SIZE_MESSAGE) Integer size
    ) {
        // 파라미터 이상시 default가 좋을까 예외 던지는 게 좋을까

        SearchResponse search = searchService.search(query, sort, page, size);

        return ApiResponse.success(search);
    }

    @GetMapping("/top/{size}")
    public String listTopQueries(@RequestParam String query, @PathVariable Integer size) {

        return query;
    }

}
