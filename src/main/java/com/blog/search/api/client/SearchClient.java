package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalClientResponse;
import com.blog.search.api.client.external.ExternalContentsProvider;
import com.blog.search.api.client.external.ExternalFeignClient;
import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SearchClient {

    private final ApplicationContext applicationContext;

    private final List<ExternalClientAdapter> adapters;

    private List<ExternalFeignClient> clients;

    @PostConstruct
    public void init() {

        // 통신은 우선순위 있음
        clients = new ArrayList<>();

        // 클라이언트 빈 모두 조회
        Map<String, ExternalFeignClient> clientBeans = applicationContext.getBeansOfType(ExternalFeignClient.class);

        // Enum 순서대로 리스트에 클리이언트 빈 추가
        ExternalContentsProvider[] providers = ExternalContentsProvider.values();
        for (ExternalContentsProvider provider : providers) {

            // 컨텐츠 제공자 Enum 이름 소문자와 빈 이름에 일치하는 부분으로 찾기
            String providerName = provider.name().toLowerCase();
            Optional<String> beanName = clientBeans.keySet().stream().filter(k -> k.contains(providerName)).findFirst();
            if (beanName.isPresent()) {
                clients.add(clientBeans.get(beanName.get()));
            }
        }
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

    private SearchClientResponse searchExternal(ExternalFeignClient client, String query, String sort, Integer page, Integer size) {

        // 어댑터별로 지원 여부 확인하고 지원시 검색 API 호출
        for (ExternalClientAdapter adapter : adapters) {
            if (adapter.supports(client)) {
                ExternalClientResponse externalResponse = adapter.search(client, query, sort, page, size);
                return SearchClientResponse.from(externalResponse);
            }
        }

        throw new SearchBusinessException(ApiResult.외부_API_통신_실패);
    }
}
