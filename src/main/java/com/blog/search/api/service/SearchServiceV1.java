package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import com.blog.search.api.client.SearchClientResponse;
import com.blog.search.api.dto.SearchResponse;
import com.blog.search.api.dto.TopQueryListResponse;
import com.blog.search.api.entity.QueryMeta;
import com.blog.search.api.repository.QueryMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceV1 implements SearchService {

    private final SearchClient searchClient;
    private final QueryMetaRepository queryMetaRepository;

    @Override
    public SearchResponse search(String query, String sort, Integer page, Integer size) {
        SearchClientResponse response = searchClient.search(query, sort, page, size);

        return new SearchResponse(response, page, size);
    }

    @Override
    public TopQueryListResponse listTopQueries() {
        List<QueryMeta> topQueries = queryMetaRepository.findTop10ByOrderBySearchCountDesc();

        return new TopQueryListResponse(topQueries);
    }
}
