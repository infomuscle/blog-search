package com.blog.search.api.dto;

import com.blog.search.api.client.SearchClientResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ApiModel(value = "SearchPage", description = "페이지 처리된 검색 결과")
public class SearchPage {

    @ApiModelProperty(value = "정렬 기준(accuracy||recency)", example = "accuracy")
    private String sort;

    @ApiModelProperty(value = "페이지 번호", example = "1")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기", example = "10")
    private Integer size;

    @ApiModelProperty(value = "전체 데이터 개수", example = "100")
    private Integer totalCount;

    @ApiModelProperty(value = "전체 페이지 크기", example = "10")
    private Integer totalPageCount;

    @ApiModelProperty(value = "블로그 게시글 리스트")
    private List<Post> posts;

    public SearchPage(SearchClientResponse source, String sort, Integer page, Integer size) {
        this.sort = sort;
        this.page = page;
        this.size = size;
        this.totalCount = source.getTotalCount();
        this.totalPageCount = (source.getTotalCount() % size == 0) ? source.getTotalCount() / size : source.getTotalCount() / size + 1;
        this.posts = source.getPosts().stream().map(Post::new).collect(Collectors.toList());
    }

    @Getter
    @ApiModel(value = "Post", description = "블로그 게시글")
    public static class Post {

        @ApiModelProperty(value = "제목", example = "오늘의 일기")
        private String title;

        @ApiModelProperty(value = "내용", example = "카카오뱅크 가고싶다")
        private String contents;

        @ApiModelProperty(value = "URL", example = "https://bortfolio.tistory.com")
        private String url;

        @ApiModelProperty(value = "게시일", example = "2023-03-22")
        private LocalDate postDate;

        public Post(SearchClientResponse.Post source) {
            this.title = source.getTitle();
            this.contents = source.getContents();
            this.url = source.getUrl();
            this.postDate = source.getPostDate();
        }
    }
}
