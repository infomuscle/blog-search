package com.blog.search.api.client.external;

public interface ExternalFeignClient {
    ExternalClientResponse search(String query, String sort, Integer page, Integer size);
}
