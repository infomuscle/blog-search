package com.blog.search.api.client.external;

public interface ExternalClientAdapter {

    Boolean supports(ExternalFeignClient client);

    ExternalSearchResponse search(ExternalFeignClient client, String query, String sort, Integer page, Integer size);

}
