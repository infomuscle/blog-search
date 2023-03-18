package com.blog.search.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Query {

    @Id
    @GeneratedValue
    private Long id;

    private Long searchCount;

}
