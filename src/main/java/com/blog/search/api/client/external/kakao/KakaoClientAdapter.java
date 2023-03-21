package com.blog.search.api.client.external.kakao;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalClientResponse;
import com.blog.search.api.client.external.ExternalFeignClient;
import org.springframework.stereotype.Component;

@Component
public class KakaoClientAdapter implements ExternalClientAdapter {
    @Override
    public Boolean supports(ExternalFeignClient client) {
        return client instanceof KakaoFeignClient;
    }

    @Override
    public ExternalClientResponse search(ExternalFeignClient client, String query, String sort, Integer page, Integer size) {
        return client.search(query, sort, page, size);
    }
}
