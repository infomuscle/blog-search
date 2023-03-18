package com.blog.search.api.dto;

import com.blog.search.api.client.SearchClientResponse;
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

    public SearchResponse(SearchClientResponse source, Integer page, Integer size) {
        this.page = page;
        this.size = size;
        this.totalCount = source.getTotalCount();
        this.totalPageCount = (source.getTotalCount() % size == 0) ? source.getTotalCount() / size : source.getTotalCount() / size + 1;
        this.posts = source.getPosts().stream().map(Post::new).collect(Collectors.toList());
    }

    @Getter
    public static class Post {

        private String title;
        private String contents;
        private String url;
        private LocalDate postDateTime;

        public Post(SearchClientResponse.Post source) {
            this.title = source.getTitle();
            this.contents = source.getContents();
            this.url = source.getUrl();
            this.postDateTime = source.getPostDateTime();
        }
    }
}
