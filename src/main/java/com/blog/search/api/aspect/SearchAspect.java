package com.blog.search.api.aspect;

import com.blog.search.api.repository.QueryRepository;
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

    private final QueryRepository queryRepository;

    @Around(value = "execution(* com.blog.search.api.service.*.search(..))")
    public Object aroundOrder(ProceedingJoinPoint pjp) throws Throwable {

        Object proceed = pjp.proceed();

        return proceed;
    }

    @Transactional
    public void save(String query) {

    }
}
