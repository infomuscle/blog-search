package com.blog.search.api.repository;

import com.blog.search.api.entity.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {

    Optional<SearchQuery> findByQueryValue(String queryValue);

    List<SearchQuery> findTop10ByOrderBySearchCountDesc();

}
