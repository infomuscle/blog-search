package com.blog.search.api.dto;

import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "ApiResponse", description = "공통 응답 DTO")
public class ApiResponse<T> {

    @ApiModelProperty(value = "HTTP 상태 코드", example = "200")
    private Integer status;

    @ApiModelProperty(value = "응답 메시지", example = "정상")
    private String message;

    @ApiModelProperty(value = "API별 응답 데이터")
    private T data;


    /**
     * 정상 응답
     */
    public ApiResponse(T data) {
        this.status = ApiResult.정상.getHttpStatus();
        this.message = ApiResult.정상.getMessage();
        this.data = data;
    }

    /**
     * 기본 오류 메시지
     */
    public ApiResponse(SearchBusinessException e) {
        this.status = e.getApiResult().getHttpStatus();
        this.message = e.getApiResult().getMessage();
    }

    /**
     * 기본 오류 메시지에 추가 메시지를 넣을 때 사용
     */
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
