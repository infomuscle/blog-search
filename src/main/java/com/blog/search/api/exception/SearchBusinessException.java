package com.blog.search.api.exception;

import com.blog.search.api.constant.ApiResult;
import lombok.Getter;

@Getter
public class SearchBusinessException extends RuntimeException {

    private ApiResult apiResult;

    public SearchBusinessException(ApiResult apiResult) {
        this.apiResult = apiResult;
    }
}
