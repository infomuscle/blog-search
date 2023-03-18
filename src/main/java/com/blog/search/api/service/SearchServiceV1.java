package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import com.blog.search.api.client.SearchClientResponse;
import com.blog.search.api.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceV1 implements SearchService {

    private final SearchClient searchClient;

    @Override
    public SearchResponse search(String query, String sort, Integer page, Integer size) {
        SearchClientResponse response = searchClient.search(query, sort, page, size);

        return new SearchResponse(response, page, size);
    }

    @Override
    public void listTopQueries() {

    }
}
