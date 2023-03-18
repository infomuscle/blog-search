package com.blog.search.api.repository;

import com.blog.search.api.entity.QueryMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryMetaRepository extends JpaRepository<QueryMeta, Long> {


    List<QueryMeta> findTop10ByOrderBySearchCountDesc();

}
