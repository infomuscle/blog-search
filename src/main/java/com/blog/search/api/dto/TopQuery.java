package com.blog.search.api.dto;

import com.blog.search.api.entity.SearchQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "TopQuery", description = "인기 검색어")
public class TopQuery {

    @ApiModelProperty(value = "검색어", example = "jpa")
    private String query;

    @ApiModelProperty(value = "검색횟수", example = "100")
    private Long searchCount;

    public TopQuery(SearchQuery searchQuery) {
        this.query = searchQuery.getQueryValue();
        this.searchCount = searchQuery.getSearchCount();
    }
}

