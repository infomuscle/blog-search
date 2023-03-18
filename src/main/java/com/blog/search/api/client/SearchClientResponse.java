package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalSearchResponse;
import com.blog.search.api.client.external.kakao.message.KakaoSearchResponse;
import com.blog.search.api.client.external.naver.message.NaverSearchResponse;
import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchClientResponse {

    private Integer totalCount;
    private List<Post> posts;

    public static SearchClientResponse from(ExternalSearchResponse source) {
        if (source instanceof KakaoSearchResponse) {
            return new SearchClientResponse((KakaoSearchResponse) source);
        }

        if (source instanceof NaverSearchResponse) {
            return new SearchClientResponse((NaverSearchResponse) source);
        }

        throw new SearchBusinessException(ApiResult.지원하지_않는_외부_API);
    }

    public SearchClientResponse(KakaoSearchResponse source) {
        this.totalCount = source.getMeta().getTotalCount();
        this.posts = source.getDocuments().stream().map(Post::new).collect(Collectors.toList());
    }

    public SearchClientResponse(NaverSearchResponse source) {
        this.totalCount = source.getTotal();
        this.posts = source.getItems().stream().map(Post::new).collect(Collectors.toList());
    }


    @Getter
    public static class Post {

        private String title;
        private String contents;
        private String url;
        private LocalDate postDateTime;

        public Post(KakaoSearchResponse.Document source) {
            this.title = source.getTitle();
            this.contents = source.getContents();
            this.url = source.getUrl();
            this.postDateTime = source.getDatetime().toLocalDate();
        }

        public Post(NaverSearchResponse.Item source) {
            this.title = source.getTitle();
            this.contents = source.getDescription();
            this.url = source.getLink();
            this.postDateTime = LocalDate.parse(source.getPostdate(), DateTimeFormatter.BASIC_ISO_DATE);
        }
    }
}

