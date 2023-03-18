package com.blog.search.api.dto;

import com.blog.search.api.client.dto.ClientSearchResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchResponse {

    private Integer page;
    private Integer size;
    private Integer totalCount;
    private Integer totalPageCount;
    private List<Post> posts;

    public SearchResponse(ClientSearchResponse source, Integer page, Integer size) {
        this.page = page;
        this.size = size;
        this.totalCount = source.getTotalCount();
        this.posts = source.getPosts().stream().map(Post::new).collect(Collectors.toList());
    }

    @Getter
    public static class Post {

        private String title;
        private String contents;
        private String url;
        private LocalDate postDateTime;

        public Post(ClientSearchResponse.Post source) {
            this.title = source.getTitle();
            this.contents = source.getContents();
            this.url = source.getUrl();
            this.postDateTime = source.getPostDateTime();
        }
    }
}
