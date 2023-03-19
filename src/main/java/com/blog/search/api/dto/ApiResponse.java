package com.blog.search.api.dto;

import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;


    public ApiResponse(T data) {
        this.status = ApiResult.정상.getHttpStatus();
        this.message = ApiResult.정상.getMessage();
        this.data = data;
    }

    public ApiResponse(SearchBusinessException e) {
        this.status = e.getApiResult().getHttpStatus();
        this.message = e.getApiResult().getMessage();
    }

    public ApiResponse(SearchBusinessException e, String additionalMessage) {
        this.status = e.getApiResult().getHttpStatus();
        this.message = new StringBuilder().append(e.getApiResult().getMessage()).append(" ").append(additionalMessage).toString();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> fail(SearchBusinessException e) {
        return new ApiResponse<>(e);
    }

    public static <T> ApiResponse<T> fail(SearchBusinessException e, String additionalMessage) {
        return new ApiResponse<>(e, additionalMessage);
    }
}
