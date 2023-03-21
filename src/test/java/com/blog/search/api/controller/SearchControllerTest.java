package com.blog.search.api.controller;

import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.entity.SearchQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("[검색] 통합 테스트")
class SearchControllerTest {

    private static final String URL_LIST_TOP_QUERIS = "/blog/v1/search/top";
    private static final String URL_SEARCH = "/blog/v1/search";
    private static final String[] MOCK_QUERIES = new String[] {"intellij", "스프링부트", "sqld", "postgresql", "mysql", "카카오", "naver", "테스트코드", "레디스", "kafka", "rabbitmq", "spring boot", "gradle 그레이들", "시스템 설계", "db 인덱스"};
    private static final Long[] MOCK_SEARCH_COUNTS = new Long[] {482402858L, 274960282L, 94284283L, 58692383L, 52352341L, 7293482L, 4451345L, 64313L, 54334L, 9353L, 7532L, 3341L, 245L, 42L, 8L};

    @Autowired(required = false)
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory emf;

    @BeforeAll
    public void init() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (int i = 0; i < MOCK_QUERIES.length; i++) {
            SearchQuery searchQuery = new SearchQuery(MOCK_QUERIES[i]);
            ReflectionTestUtils.setField(searchQuery, "searchCount", MOCK_SEARCH_COUNTS[i]);
            em.persist(searchQuery);
        }
        tx.commit();
    }

    @Nested
    @DisplayName("[검색]")
    class Search {

        @ParameterizedTest
        @DisplayName("[성공]")
        @CsvSource(value = {"jpa, accuracy, 1, 10", "jpa, accuracy, 2, 5", "jpa, recency, 3, 50"}, nullValues = "null")
        public void testSuccess(String query, String sort, Integer page, Integer size) throws Exception {
            MultiValueMap<String, String> parmas = createParmas(query, sort, page, size);
            performGet(URL_SEARCH, parmas)
                    .andExpect(jsonPath("$.status", is(ApiResult.정상.getHttpStatus())))
                    .andExpect(jsonPath("$.message", is(ApiResult.정상.getMessage())))
                    .andExpect(jsonPath("$.data.page", is(page)))
                    .andExpect(jsonPath("$.data.size", is(size)))
                    .andExpect(jsonPath("$.data.totalCount", greaterThan(0)))
                    .andExpect(jsonPath("$.data.totalPageCount", greaterThan(0)))
                    .andExpect(jsonPath("$.data.posts", hasSize(size)))
            ;
        }

        @ParameterizedTest
        @DisplayName("[실패] 필수 파라미터 누락")
        @CsvSource(value = {"null, accuracy, 1, 10", "jpa, null, 1, 10", "jpa, accuracy, null, 10", "jpa, accuracy, 1, null"}, nullValues = "null")
        public void testFailMissingParameter(String query, String sort, Integer page, Integer size) throws Exception {
            MultiValueMap<String, String> parmas = createParmas(query, sort, page, size);
            performGet(URL_SEARCH, parmas)
                    .andExpect(jsonPath("$.status", is(ApiResult.필수_파라미터_누락.getHttpStatus())))
                    .andExpect(jsonPath("$.message", is(ApiResult.필수_파라미터_누락.getMessage())))
                    .andExpect(jsonPath("$.data", nullValue()))
            ;
        }

        @ParameterizedTest
        @DisplayName("[실패] 파라미터 검증 실패")
        @CsvSource(value = {"jpa, test, 1, 10", "jpa, accuracy, 0, 10", "jpa, accuracy, 51, 10", "jpa, accuracy, 1, 0", "jpa, accuracy, 1, 51"})
        public void testFailConstraintViolation(String query, String sort, Integer page, Integer size) throws Exception {
            MultiValueMap<String, String> parmas = createParmas(query, sort, page, size);
            performGet(URL_SEARCH, parmas)
                    .andExpect(jsonPath("$.status", is(ApiResult.파라미터_검증_실패.getHttpStatus())))
                    .andExpect(jsonPath("$.message", containsString(ApiResult.파라미터_검증_실패.getMessage())))
                    .andExpect(jsonPath("$.data", nullValue()))
            ;
        }

        // 카카오 실패
    }


    @Nested
    @DisplayName("[인기검색어]")
    class ListTopQueries {

        @DisplayName("[성공] 검색횟수 증가 반영")
        @ParameterizedTest
        @CsvSource({"0", "1", "2", "3"})
        public void testSuccessAfterSearch(Integer count) throws Exception {

            MultiValueMap<String, String> params = createParmas(MOCK_QUERIES[count], "accuracy", 1, 10);
            for (Integer i = 0; i < count; i++) {
                performGet(URL_SEARCH, params);
            }

            performGet(URL_LIST_TOP_QUERIS)
                    .andExpect(jsonPath("$.status", is(ApiResult.정상.getHttpStatus())))
                    .andExpect(jsonPath("$.message", is(ApiResult.정상.getMessage())))
                    .andExpect(jsonPath("$.data", hasSize(10)))
                    .andExpect(jsonPath(String.format("$.data[%s].query", count), is(MOCK_QUERIES[count])))
                    .andExpect(jsonPath(String.format("$.data[%s].searchCount", count), is(MOCK_SEARCH_COUNTS[count].intValue() + count)))
            ;
        }
    }

    private MultiValueMap<String, String> createParmas(String query, String sort, Integer page, Integer size) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (query != null) {
            params.set("query", query);
        }
        if (sort != null) {
            params.set("sort", sort);
        }
        if (page != null) {
            params.set("page", page.toString());
        }
        if (size != null) {
            params.set("size", size.toString());
        }

        return params;
    }

    private ResultActions performGet(String url) {
        return performGet(url, new LinkedMultiValueMap<>());
    }

    private ResultActions performGet(String url, MultiValueMap<String, String> params) {
        try {
            return mockMvc.perform(
                    get(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(params)
            ).andDo(print());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
