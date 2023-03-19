package com.blog.search.api.advice;

import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.dto.ApiResponse;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class SearchControllerAdivce {

    /**
     * 핸들링된 예외
     */
    @ExceptionHandler
    public ResponseEntity handleException(SearchBusinessException e) {
        log.info(e.getMessage(), e);

        return ResponseEntity.status(e.getApiResult().getHttpStatus()).body(ApiResponse.fail(e));
    }

    @ExceptionHandler
    public ResponseEntity handleException(MissingServletRequestParameterException e) {
        log.info(e.getMessage(), e);

        SearchBusinessException exception = new SearchBusinessException(ApiResult.필수_파라미터_누락);

        return ResponseEntity.badRequest().body(ApiResponse.fail(exception));
    }

    @ExceptionHandler
    public ResponseEntity handleException(ConstraintViolationException e) {
        log.info(e.getMessage(), e);

        SearchBusinessException exception = new SearchBusinessException(ApiResult.파라미터_검증_실패);
        List<String> additionalMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.info("SearchControllerAdivce.handleException# additionalMessages: {} ", additionalMessages);

        return ResponseEntity.badRequest().body(ApiResponse.fail(exception, String.join(" ", additionalMessages)));
    }

    /**
     * 핸들링 안 된 예외
     */
    @ExceptionHandler
    public ResponseEntity handleException(Exception e) {
        log.info(e.getMessage(), e);

        SearchBusinessException exception = new SearchBusinessException(ApiResult.기타_오류);

        return ResponseEntity.internalServerError().body(ApiResponse.fail(exception));
    }
}
