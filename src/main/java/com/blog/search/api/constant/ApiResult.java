package com.blog.search.api.constant;

import lombok.Getter;

@Getter
public enum ApiResult {

    // 200~
    정상(200, "0000", "정상"),

    // 400~

    // 500~
    지원하지_않는_외부_API(500, "5001", "지원하지 않는 외부 API입니다");

    private String code;
    private String message;
    private Integer httpStatus;

    ApiResult(Integer httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
