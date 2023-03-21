package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalClientResponse;
import com.blog.search.api.client.external.kakao.message.KakaoClientResponse;
import com.blog.search.api.client.external.naver.message.NaverClientResponse;
import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchClientResponse {

    // 하위 모듈 의존성 제거하려고 하위 모듈에 상위 모듈 변환 로직 추가하는 게 좋을까?

    private Integer totalCount;
    private List<Post> posts;

    public static SearchClientResponse from(ExternalClientResponse source) {
        if (source instanceof KakaoClientResponse) {
            return new SearchClientResponse((KakaoClientResponse) source);
        }

        if (source instanceof NaverClientResponse) {
            return new SearchClientResponse((NaverClientResponse) source);
        }

        throw new SearchBusinessException(ApiResult.지원하지_않는_외부_API);
    }

    public SearchClientResponse(KakaoClientResponse source) {
        this.totalCount = source.getMeta().getTotalCount();
        this.posts = source.getDocuments().stream().map(Post::new).collect(Collectors.toList());
    }

    public SearchClientResponse(NaverClientResponse source) {
        this.totalCount = source.getTotal();
        this.posts = source.getItems().stream().map(Post::new).collect(Collectors.toList());
    }


    @Getter
    public static class Post {

        private String title;
        private String contents;
        private String url;
        private LocalDate postDateTime;

        public Post(KakaoClientResponse.Document source) {
            this.title = source.getTitle();
            this.contents = source.getContents();
            this.url = source.getUrl();
            this.postDateTime = source.getDatetime().toLocalDate();
        }

        public Post(NaverClientResponse.Item source) {
            this.title = source.getTitle();
            this.contents = source.getDescription();
            this.url = source.getLink();
            this.postDateTime = LocalDate.parse(source.getPostdate(), DateTimeFormatter.BASIC_ISO_DATE);
        }
    }
}

