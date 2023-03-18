package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import com.blog.search.api.client.dto.ClientSearchResponse;
import com.blog.search.api.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceV1 implements SearchService {

    private final SearchClient searchClient;

    @Override
    public SearchResponse search(String query, String sort, Integer page, Integer size) {
        ClientSearchResponse response = searchClient.search(query, sort, page, size);

        return new SearchResponse(response, page, size);
    }

    @Override
    public void listTopQueries() {

    }
}
