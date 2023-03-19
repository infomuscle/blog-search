package com.blog.search.api.controller;

import com.blog.search.api.constant.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("[검색] 통합 테스트")
class SearchControllerTest {

    private ObjectMapper om = new ObjectMapper();

    @Autowired(required = false)
    private MockMvc mockMvc;


    @ParameterizedTest
    @CsvSource(value = {"jpa, accuracy, 1, 10", "jpa, accuracy, 2, 5", "jpa, recency, 3, 50"}, nullValues = "null")
    public void testSuccess(String query, String sort, Integer page, Integer size) throws Exception {
        performGet(query, sort, page, size)
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
    @CsvSource(value = {"null, accuracy, 1, 10", "jpa, null, 1, 10", "jpa, accuracy, null, 10", "jpa, accuracy, 1, null"}, nullValues = "null")
    public void testFailMissingParameter(String query, String sort, Integer page, Integer size) throws Exception {
        performGet(query, sort, page, size)
                .andExpect(jsonPath("$.status", is(ApiResult.필수_파라미터_누락.getHttpStatus())))
                .andExpect(jsonPath("$.message", is(ApiResult.필수_파라미터_누락.getMessage())))
                .andExpect(jsonPath("$.data", nullValue()))
        ;
    }

    @ParameterizedTest
    @CsvSource(value = {"jpa, accuracy, 0, 10", "jpa, accuracy, 51, 10", "jpa, accuracy, 1, 0", "jpa, accuracy, 1, 51"})
    public void testFailConstraintViolation(String query, String sort, Integer page, Integer size) throws Exception {
        performGet(query, sort, page, size)
                .andExpect(jsonPath("$.status", is(ApiResult.파라미터_검증_실패.getHttpStatus())))
                .andExpect(jsonPath("$.message", containsString(ApiResult.파라미터_검증_실패.getMessage())))
                .andExpect(jsonPath("$.data", nullValue()))
        ;
    }

    private ResultActions performGet(String query, String sort, Integer page, Integer size) {
        try {

            MultiValueMap<String, String> queryMap = new LinkedMultiValueMap<>();
            if (query != null) {
                queryMap.set("query", query);
            }
            if (sort != null) {
                queryMap.set("sort", sort);
            }
            if (page != null) {
                queryMap.set("page", page.toString());
            }
            if (size != null) {
                queryMap.set("size", size.toString());
            }

            return mockMvc.perform(
                    get("/blog/v1/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(queryMap)
            ).andDo(print());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
