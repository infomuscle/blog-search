package com.blog.search.api.service;

import com.blog.search.api.dto.SearchResponse;

public interface SearchService {

    SearchResponse search(String query, String sort, Integer page, Integer size);

    void listTopQueries();
}
