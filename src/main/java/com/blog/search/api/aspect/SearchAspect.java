package com.blog.search.api.aspect;

import com.blog.search.api.entity.SearchQuery;
import com.blog.search.api.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class SearchAspect {

    private final SearchQueryRepository searchQueryRepository;

    // 검색어 저장은 검색 서비스의 부가적인 기능으로 판단
    // 이후 검색 서비스의 수정, 확장에 영향 받지 않도록 분리
    @Around(value = "execution(* com.blog.search.api.service.*.search(..)) && args(query, ..)")
    public Object aroundSearch(ProceedingJoinPoint pjp, String query) throws Throwable {
        updateQuerySearchCount(query);

        return pjp.proceed();
    }

    // 실제 구현한다면 Kafka
    // 과제에서는 직접 DB 호출
    @Transactional
    public void updateQuerySearchCount(String query) {
        SearchQuery searchQuery = searchQueryRepository.findByQueryValue(query).orElse(new SearchQuery(query));
        searchQuery.updateSearchCount();
        searchQueryRepository.save(searchQuery);
    }
}
