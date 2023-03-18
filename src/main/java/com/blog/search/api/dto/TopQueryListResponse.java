package com.blog.search.api.dto;

import com.blog.search.api.entity.QueryMeta;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TopQueryListResponse {

    private List<TopQuery> topQueries;

    @Getter
    public static class TopQuery {

        private String query;
        private Integer searchCount;

        public TopQuery(QueryMeta source) {
            this.query = source.getQueryValue();
            this.searchCount = source.getSearchCount();
        }
    }

    public TopQueryListResponse(List<QueryMeta> queryMetas) {
        this.topQueries = queryMetas.stream().map(TopQuery::new).collect(Collectors.toList());
    }
}

