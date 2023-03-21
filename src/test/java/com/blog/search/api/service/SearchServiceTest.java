package com.blog.search.api.service;

import com.blog.search.api.client.SearchClient;
import com.blog.search.api.client.SearchClientResponse;
import com.blog.search.api.dto.SearchPage;
import com.blog.search.api.dto.TopQuery;
import com.blog.search.api.entity.SearchQuery;
import com.blog.search.api.repository.SearchQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] 서비스")
class SearchServiceTest {

    @InjectMocks
    SearchServiceV1 searchService;

    @Mock
    SearchClient searchClient;

    @Mock
    SearchQueryRepository searchQueryRepository;


    @Nested
    @DisplayName("[검색]")
    class Search {

        @ParameterizedTest
        @DisplayName("[성공]")
        @CsvSource({"jpa, accuracy, 1, 10"})
        void testSuccess(String query, String sort, Integer page, Integer size) {

            // given
            List<SearchClientResponse.Post> posts = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                posts.add(new SearchClientResponse.Post());
            }

            SearchClientResponse searchClientResponse = new SearchClientResponse();
            ReflectionTestUtils.setField(searchClientResponse, "totalCount", size);
            ReflectionTestUtils.setField(searchClientResponse, "posts", posts);

            Mockito.when(searchClient.search(query, sort, page, size)).thenReturn(searchClientResponse);

            // when
            SearchPage searchPage = searchService.search(query, sort, page, size);

            // then
            assertThat(searchPage.getSort()).isEqualTo(sort);
            assertThat(searchPage.getPage()).isEqualTo(page);
            assertThat(searchPage.getSize()).isEqualTo(size);
            assertThat(searchPage.getTotalCount()).isEqualTo(size);
            assertThat(searchPage.getTotalPageCount()).isEqualTo((searchPage.getTotalCount() % size == 0) ? searchPage.getTotalCount() / size : searchPage.getTotalCount() / size + 1);
            assertThat(searchPage.getPosts()).hasSize(size);
        }
    }


    @Nested
    @DisplayName("[인기 검색어 조회]")
    class ListTopQueries {

        @Test
        @DisplayName("[성공]")
        void testSuccess() {

            // given
            List<SearchQuery> searchQueries = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                searchQueries.add(new SearchQuery());
            }

            Mockito.when(searchQueryRepository.findTop10ByOrderBySearchCountDesc()).thenReturn(searchQueries);

            // when
            List<TopQuery> topQueries = searchService.listTopQueries();

            assertThat(topQueries).hasSize(10);
        }
    }
}
