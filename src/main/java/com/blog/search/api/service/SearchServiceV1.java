package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceV1 implements SearchService {

    private final SearchClient searchClient;


    @Override
    public void search(String query, String sort, Integer page, Integer size) {
        log.info("SearchServiceV1.search# query: {} ", query);

        String response = searchClient.search(query, sort, page, size);
        log.info("SearchServiceV1.search# response: {} ", response);


    }

    @Override
    public void listTopQueries() {

    }
}
