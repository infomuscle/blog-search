package com.blog.search.api.constant;

import lombok.Getter;

@Getter
public enum ApiResult {

    // 200~
    정상(200, "0000", "정상"),

    // 400~

    // 500~
    지원하지_않는_외부_API(500, "5001", "지원하지 않는 외부 API입니다"),
    외부_API_통신_실패(500, "5002", "외부 API 통신이 실패했습니다."),
    기타_오류(500, "5999", "처리 중 오류가 발생했습니다.");

    private String code;
    private String message;
    private Integer httpStatus;

    ApiResult(Integer httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
