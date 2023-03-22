package com.blog.search.api.client;

import com.blog.search.api.client.external.ExternalClientAdapter;
import com.blog.search.api.client.external.ExternalFeignClient;
import com.blog.search.api.client.external.kakao.KakaoClientAdapter;
import com.blog.search.api.client.external.kakao.KakaoFeignClient;
import com.blog.search.api.client.external.kakao.message.KakaoClientResponse;
import com.blog.search.api.client.external.naver.NaverClientAdapter;
import com.blog.search.api.client.external.naver.NaverFeignClient;
import com.blog.search.api.client.external.naver.message.NaverClientResponse;
import com.blog.search.api.constant.ApiResult;
import com.blog.search.api.exception.SearchBusinessException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@DisplayName("[클라이언트] 단위 테스트")
class SearchClientTest {

    @InjectMocks
    private SearchClient searchClient;

    @Spy
    private List<ExternalClientAdapter> adapters = new ArrayList<>();

    @Mock
    private KakaoFeignClient kakaoFeignClient;

    @Mock
    private NaverFeignClient naverFeignClient;

    @Mock
    private KakaoClientAdapter kakaoClientAdapter;

    @Mock
    private NaverClientAdapter naverClientAdapter;

    @BeforeEach
    public void init() {
        List<ExternalFeignClient> clients = new ArrayList<>();
        clients.add(kakaoFeignClient);
        clients.add(naverFeignClient);

        adapters.add(kakaoClientAdapter);
        adapters.add(naverClientAdapter);

        ReflectionTestUtils.setField(searchClient, "clients", clients);
    }

    @ParameterizedTest
    @CsvSource({"10"})
    @DisplayName("[성공] 카카오 성공")
    public void testSuccessKakao(Integer size) {

        // given
        List<KakaoClientResponse.Document> documents = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            KakaoClientResponse.Document document = new KakaoClientResponse.Document();
            ReflectionTestUtils.setField(document, "title", "title" + i);
            ReflectionTestUtils.setField(document, "contents", "contents" + i);
            ReflectionTestUtils.setField(document, "url", "url" + i);
            ReflectionTestUtils.setField(document, "datetime", LocalDate.parse("20230322", DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay());
            documents.add(document);
        }

        KakaoClientResponse.Meta meta = new KakaoClientResponse.Meta();
        ReflectionTestUtils.setField(meta, "totalCount", size);

        KakaoClientResponse kakaoClientResponse = new KakaoClientResponse();
        ReflectionTestUtils.setField(kakaoClientResponse, "documents", documents);
        ReflectionTestUtils.setField(kakaoClientResponse, "meta", meta);

        Mockito.when(kakaoClientAdapter.supports(kakaoFeignClient)).thenReturn(true);
        Mockito.when(kakaoClientAdapter.search(any(), any(), any(), any(), any())).thenReturn(kakaoClientResponse);

        // when
        SearchClientResponse searchClientResponse = searchClient.search("jpa", "accuracy", 1, size);

        // then
        assertThat(searchClientResponse.getTotalCount()).isEqualTo(size);
        for (int i = 0; i < searchClientResponse.getPosts().size(); i++) {
            SearchClientResponse.Post post = searchClientResponse.getPosts().get(i);
            assertThat(post.getTitle()).isEqualTo("title" + i);
            assertThat(post.getContents()).isEqualTo("contents" + i);
            assertThat(post.getUrl()).isEqualTo("url" + i);
            assertThat(post.getPostDate()).isEqualTo(LocalDate.parse("20230322", DateTimeFormatter.BASIC_ISO_DATE));
        }
    }

    @ParameterizedTest
    @CsvSource({"10"})
    @DisplayName("[성공] 카카오 실패 후 네이버 성공")
    public void testFailKakao(Integer size) {

        // given
        List<NaverClientResponse.Item> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NaverClientResponse.Item item = new NaverClientResponse.Item();
            ReflectionTestUtils.setField(item, "title", "title" + i);
            ReflectionTestUtils.setField(item, "description", "description" + i);
            ReflectionTestUtils.setField(item, "link", "link" + i);
            ReflectionTestUtils.setField(item, "postdate", "20230322");
            items.add(item);
        }

        NaverClientResponse naverClientResponse = new NaverClientResponse();
        ReflectionTestUtils.setField(naverClientResponse, "total", size);
        ReflectionTestUtils.setField(naverClientResponse, "items", items);

        Mockito.when(naverClientAdapter.supports(naverFeignClient)).thenReturn(true);
        Mockito.when(naverClientAdapter.search(any(), any(), any(), any(), any())).thenReturn(naverClientResponse);

        // when
        SearchClientResponse searchClientResponse = searchClient.search("jpa", "accuracy", 1, size);

        // then
        assertThat(searchClientResponse.getTotalCount()).isEqualTo(size);
        for (int i = 0; i < searchClientResponse.getPosts().size(); i++) {
            SearchClientResponse.Post post = searchClientResponse.getPosts().get(i);
            assertThat(post.getTitle()).isEqualTo("title" + i);
            assertThat(post.getContents()).isEqualTo("description" + i);
            assertThat(post.getUrl()).isEqualTo("link" + i);
            assertThat(post.getPostDate()).isEqualTo(LocalDate.parse("20230322", DateTimeFormatter.BASIC_ISO_DATE));
        }
    }

    @Test
    @DisplayName("[실패] 카카오, 네이버 모두 실패")
    public void testFailAll() {

        // given

        // when
        AbstractThrowableAssert<?, ? extends Throwable> throwableAssert = assertThatThrownBy(() -> searchClient.search("jpa", "accuracy", 1, 10));

        // then
        throwableAssert.isInstanceOf(SearchBusinessException.class).hasMessage(ApiResult.외부_API_통신_실패.getMessage());
    }
}
