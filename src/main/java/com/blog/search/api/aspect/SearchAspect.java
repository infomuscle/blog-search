package com.blog.search.api.aspect;

import com.blog.search.api.entity.Query;
import com.blog.search.api.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SearchAspect {

    private final QueryRepository queryRepository;

    @Around(value = "execution(* com.blog.search.api.service.*.search(..)) && args(query, ..)")
    public Object aroundSearch(ProceedingJoinPoint pjp, String query) throws Throwable {
        save(query);

        return pjp.proceed();
    }

    // 실제로 한다면 따로 미들웨어를 두지 않을까
    @Transactional
    public void save(String query) {
        queryRepository.save(new Query(query));
    }
}
