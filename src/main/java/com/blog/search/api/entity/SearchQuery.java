package com.blog.search.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class SearchQuery extends Audit {

    @Id
    @GeneratedValue
    private Long id;

    private String queryValue;

    private Long searchCount;

    public SearchQuery(String queryValue) {
        this.queryValue = queryValue;
        this.searchCount = 0L;
    }

    public Long updateSearchCount() {
        return ++searchCount;
    }
}
