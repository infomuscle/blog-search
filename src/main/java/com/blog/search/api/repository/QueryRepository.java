package com.blog.search.api.repository;

import com.blog.search.api.entity.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query, Long> {
}
