package com.blog.search.api.service;

import com.blog.search.api.dto.SearchPage;
import com.blog.search.api.dto.TopQuery;

import java.util.List;

public interface SearchService {

    SearchPage search(String query, String sort, Integer page, Integer size);

    List<TopQuery> listTopQueries();
}
