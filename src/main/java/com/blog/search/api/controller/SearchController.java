package com.blog.search.api.controller;

import com.blog.search.api.dto.ApiResponse;
import com.blog.search.api.dto.SearchPage;
import com.blog.search.api.dto.TopQuery;
import com.blog.search.api.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
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
    // 파라미터 이상시 default가 좋을까 예외 던지는 게 좋을까

    @GetMapping
    public ApiResponse search(
            @RequestParam String query,
            @RequestParam String sort,
            @RequestParam @Min(value = 1, message = INVALID_PAGE_MESSAGE) @Max(value = 50, message = INVALID_PAGE_MESSAGE) Integer page,
            @RequestParam @Min(value = 1, message = INVALID_SIZE_MESSAGE) @Max(value = 50, message = INVALID_SIZE_MESSAGE) Integer size
    ) {
        SearchPage search = searchService.search(query, sort, page, size);

        return ApiResponse.success(search);
    }

    @GetMapping("/top")
    public ApiResponse listTopQueries() {
        List<TopQuery> topQueries = searchService.listTopQueries();

        return ApiResponse.success(topQueries);
    }
}
