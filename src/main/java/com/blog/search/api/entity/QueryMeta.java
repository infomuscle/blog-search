package com.blog.search.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class QueryMeta extends Audit {

    @Id
    @GeneratedValue
    private Long id;

    private String queryValue;

    private Integer searchCount;

    public QueryMeta(String queryValue, Integer searchCount) {
        this.queryValue = queryValue;
        this.searchCount = searchCount;
    }
}
