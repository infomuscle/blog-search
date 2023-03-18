package com.blog.search.api.client.external;

public interface ExternalFeignClient {

    ExternalSearchResponse search(String query, String sort, Integer page, Integer size);
}
