package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import com.blog.search.api.client.SearchClientResponse;
import com.blog.search.api.dto.SearchPage;
import com.blog.search.api.dto.TopQuery;
import com.blog.search.api.entity.SearchQuery;
import com.blog.search.api.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceV1 implements SearchService {

    private final SearchClient searchClient;
    private final SearchQueryRepository searchQueryRepository;

    @Override
    public SearchPage search(String query, String sort, Integer page, Integer size) {

        // 외부 API 조회
        SearchClientResponse response = searchClient.search(query, sort, page, size);

        // 검색 페이지 DTO로 반환
        return new SearchPage(response, sort, page, size);
    }

    @Override
    public List<TopQuery> listTopQueries() {

        // 검색횟수 상위 10개 조회
        List<SearchQuery> searchQueries = searchQueryRepository.findTop10ByOrderBySearchCountDesc();

        // 인기검색어 DTO 리스트로 반환
        return searchQueries.stream().map(TopQuery::new).collect(Collectors.toList());
    }
}
