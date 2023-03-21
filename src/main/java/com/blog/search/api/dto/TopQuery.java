package com.blog.search.api.dto;

import com.blog.search.api.entity.SearchQuery;
import lombok.Getter;

@Getter
public class TopQuery {

    private String query;
    private Long searchCount;

    public TopQuery(SearchQuery searchQuery) {
        this.query = searchQuery.getQueryValue();
        this.searchCount = searchQuery.getSearchCount();
    }
}

