package com.blog.search.api.service;

public interface SearchService {

    void search(String query, String sort, Integer page, Integer size);

    void listTopQueries();
}
