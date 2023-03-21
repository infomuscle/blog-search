package com.blog.search.api.controller;

import com.blog.search.api.dto.ApiResponse;
import com.blog.search.api.dto.SearchPage;
import com.blog.search.api.dto.TopQuery;
import com.blog.search.api.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/v1/search")
@Api(tags = "블로그 검색 서비스")
public class SearchController {

    private static final String INVALID_SORT_MESSAGE = "정렬 조건 파라미터는 accuracy(정확도순) 또는 recency(최신순)이어야 합니다.";
    private static final String INVALID_PAGE_MESSAGE = "페이지는 1~50 범위까지 지원합니다.";
    private static final String INVALID_SIZE_MESSAGE = "페이지 크기는 1~50 범위까지 지원합니다.";

    private final SearchService searchService;

    // rest 디자인 좀 더 고민 필요
    // sort = accuracy, recency
    // 파라미터 이상시 default가 좋을까 예외 던지는 게 좋을까

    @GetMapping
    @ApiOperation(value = "검색 API", notes = "외부 검색 소스의 검색 결과를 페이징 처리하여 반환한다.")
    public ApiResponse<SearchPage> search(
            @ApiParam(name = "query", value = "검색어", required = true) @RequestParam String query,
            @ApiParam(name = "sort", value = "accuracy(정확도순), recency(최신순) 선택 가능", required = true) @RequestParam @Pattern(regexp = "^(accuracy|recency)$", message = INVALID_SORT_MESSAGE) String sort,
            @ApiParam(name = "page", value = "페이지 번호", required = true) @RequestParam @Min(value = 1, message = INVALID_PAGE_MESSAGE) @Max(value = 50, message = INVALID_PAGE_MESSAGE) Integer page,
            @ApiParam(name = "size", value = "페이지 크기", required = true) @RequestParam @Min(value = 1, message = INVALID_SIZE_MESSAGE) @Max(value = 50, message = INVALID_SIZE_MESSAGE) Integer size
    ) {
        SearchPage search = searchService.search(query, sort, page, size);

        return ApiResponse.success(search);
    }

    @GetMapping("/queries/top")
    @ApiOperation(value = "인기 검색어 조회 API", notes = "상위 10개의 인기 검색어를 반환한다.")
    public ApiResponse<List<TopQuery>> listTopQueries() {
        List<TopQuery> topQueries = searchService.listTopQueries();

        return ApiResponse.success(topQueries);
    }
}
