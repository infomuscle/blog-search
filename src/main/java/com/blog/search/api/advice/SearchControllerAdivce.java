package com.blog.search.api.advice;

import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.dto.ApiResponse;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SearchControllerAdivce {

    @ExceptionHandler
    public ResponseEntity handleSearchBusinessException(SearchBusinessException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getApiResult().getHttpStatus()).body(ApiResponse.fail(e));
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception e) {
        log.info(e.getMessage(), e);
        SearchBusinessException exception = new SearchBusinessException(ApiResult.기타_오류);
        return ResponseEntity.status(exception.getApiResult().getHttpStatus()).body(ApiResponse.fail(exception));
    }
}
