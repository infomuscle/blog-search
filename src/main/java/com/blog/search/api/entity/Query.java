package com.blog.search.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Query extends Audit {

    @Id
    @GeneratedValue
    private Long id;

    private String queryValue;

    public Query(String queryValue) {
        this.queryValue = queryValue;
    }
}
