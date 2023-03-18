package com.blog.search.api.advice;

import com.blog.search.api.exception.SearchBusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SearchControllerAdivce {

    @ExceptionHandler
    public ResponseEntity<?> handleForbiddenException(SearchBusinessException e) {
        return ResponseEntity.status(e.getApiResult().getHttpStatus()).body(e.getApiResult().getMessage());
    }
}
