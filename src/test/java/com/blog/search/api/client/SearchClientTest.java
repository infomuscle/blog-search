package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalFeignClient;
import com.blog.search.api.client.external.kakao.KakaoClientAdapter;
import com.blog.search.api.client.external.kakao.KakaoFeignClient;
import com.blog.search.api.client.external.naver.NaverClientAdapter;
import com.blog.search.api.client.external.naver.NaverFeignClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@DisplayName("[클라이언트] 단위 테스트")
class SearchClientTest {

    @InjectMocks
    private SearchClient searchClient;

    @Spy
    private List<ExternalFeignClient> clients;

    @Spy
    private List<ExternalClientAdapter> adapters;

    @Mock
    private KakaoFeignClient kakaoFeignClient;

    @Mock
    private NaverFeignClient naverFeignClient;

    @Mock
    private KakaoClientAdapter kakaoClientAdapter;

    @Mock
    private NaverClientAdapter naverClientAdapter;

    @BeforeAll
    public void setUp() {
        clients = new ArrayList<>();
        clients.add(kakaoFeignClient);
        clients.add(naverFeignClient);

        adapters = new ArrayList<>();
        adapters.add(kakaoClientAdapter);
        adapters.add(naverClientAdapter);
    }

    // @Test
    public void testFailKakao() {

        Mockito.when(kakaoFeignClient.search(any(), any(), any(), any())).thenThrow(new RuntimeException());

        searchClient.search("jpa", "accuracy", 1, 10);

    }

}
