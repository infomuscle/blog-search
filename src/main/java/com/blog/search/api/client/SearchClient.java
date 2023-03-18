package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalFeignClient;
import com.blog.search.api.client.external.ExternalSearchResponse;
import com.blog.search.api.client.external.kakao.KakaoFeignClient;
import com.blog.search.api.client.external.naver.NaverFeignClient;
import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchClient {

    private final ApplicationContext applicationContext;

    private List<ExternalFeignClient> clients;

    private List<ExternalClientAdapter> adapters;

    @PostConstruct
    public void init() {

        // 통신 우선순위
        clients = new ArrayList<>();
        clients.add(applicationContext.getBean(KakaoFeignClient.class));
        clients.add(applicationContext.getBean(NaverFeignClient.class));

        // 어댑터는 순서 X
        adapters = new ArrayList<>();
        adapters.addAll(applicationContext.getBeansOfType(ExternalClientAdapter.class).values());
    }

    public SearchClientResponse search(String query, String sort, Integer page, Integer size) {

        // 순서대로 외부 통신 시도하기
        for (ExternalFeignClient client : clients) {
            try {
                return searchExternal(client, query, sort, page, size);
            } catch (Exception e) {
                continue;
            }
        }

        throw new SearchBusinessException(ApiResult.외부_API_통신_실패);
    }

    // for? Map? 유의미한 차이가 있을까?
    private SearchClientResponse searchExternal(ExternalFeignClient client, String query, String sort, Integer page, Integer size) {
        for (ExternalClientAdapter adapter : adapters) {
            if (adapter.supports(client)) {
                ExternalSearchResponse externalResponse = adapter.search(client, query, sort, page, size);
                return SearchClientResponse.from(externalResponse);
            }
        }

        throw new SearchBusinessException(ApiResult.외부_API_통신_실패);
    }
}
