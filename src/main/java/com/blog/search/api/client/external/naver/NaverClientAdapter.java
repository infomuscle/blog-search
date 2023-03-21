package com.blog.search.api.client.external.naver;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalClientResponse;
import com.blog.search.api.client.external.ExternalFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverClientAdapter implements ExternalClientAdapter {

    private static Map<String, String> NAVER_SORT_PARAMS = Map.of("accuracy", "sim", "recency", "date");

    @Override
    public Boolean supports(ExternalFeignClient client) {
        return client instanceof NaverFeignClient;
    }

    @Override
    public ExternalClientResponse search(ExternalFeignClient client, String query, String sort, Integer page, Integer size) {

        // 네이버 정렬 기준 파라미터 매핑
        if (sort != null) {
            sort = NAVER_SORT_PARAMS.get(sort);
        }

        // 네이버 페이징 처리
        Integer start = null;
        if (page != null) {
            start = (page - 1) * size + 1;
        }

        return client.search(query, sort, start, size);
    }
}
