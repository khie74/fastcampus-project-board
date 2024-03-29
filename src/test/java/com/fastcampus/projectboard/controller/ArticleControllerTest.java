package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
// 아래처럼 쓰면 모든 컨트롤러를 읽어들인다.
//@WebMvcTest
// 그러므로 테스트 대상이 되는 컨트롤러만 로드하게 아래처럼 하면 된다.
@WebMvcTest(ArticleController.class)
// 기본 웹 시큐리티를 불러오면 테스트가 실패한다.
// 우리가 설정한 설정을 가져오게 하기 위해서, 우리가 만든 Security Config를 읽도록 추가하였다.
@Import(SecurityConfig.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    // articleService를 목킹
    // 주의! MockBean은 목킹을 할 수 없다.
    // 그래서 생성자주입을 못쓰고, 필드 주입을 한다.
    // 이와는 달리 mvc는 생성자 주입을 하였다.
    @MockBean private ArticleService articleService;

    // 테스트 패키지에서는 @Autowired를 생략할 수 없다.
    ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트(게시판) 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
                .willReturn(Page.empty());

        // When & Then
        // 'get()'을 입력할 때
        // 먼저 Sift+Ctrl+Space로 2번 불러서 목록을 더 가져오고
        // 거기서 선택한 다음, Opt+Enter를 눌러서 static으로 import할 것.
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                // 모델 애트리뷰트라는 이름의 맵에 이 이름의 키가 있는지까지만 검사
                // 모델 이름을 뭘로 할지, 그 모델이 존재하는지 정도만 체크. 데이터 정합성까지는 아니고...
                .andExpect(model().attributeExists("articles"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentDto());

        // When & Then
        // 'get()'을 입력할 때
        // 먼저 Sift+Ctrl+Space로 2번 불러서 목록을 더 가져오고
        // 거기서 선택한 다음, Opt+Enter를 눌러서 static으로 import할 것.
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                // 모델 애트리뷰트라는 이름의 맵에 이 이름의 키가 있는지까지만 검사
                // 모델 이름을 뭘로 할지, 그 모델이 존재하는지 정도만 체크. 데이터 정합성까지는 아니고...
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Give

        // When & Then
        // 'get()'을 입력할 때
        // 먼저 Sift+Ctrl+Space로 2번 불러서 목록을 더 가져오고
        // 거기서 선택한 다음, Opt+Enter를 눌러서 static으로 import할 것.
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Give

        // When & Then
        // 'get()'을 입력할 때
        // 먼저 Sift+Ctrl+Space로 2번 불러서 목록을 더 가져오고
        // 거기서 선택한 다음, Opt+Enter를 눌러서 static으로 import할 것.
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));
    }

    private ArticleWithCommentsDto createArticleWithCommentDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "sks",
                LocalDateTime.now(),
                "sks");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "sks",
                "pw",
                "abc@gmail.com",
                "sks",
                "memo",
                LocalDateTime.now(),
                "sks",
                LocalDateTime.now(),
                "sks");
    }
}
