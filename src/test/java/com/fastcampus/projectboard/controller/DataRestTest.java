package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
// @WebMvcTest를 사용하면 Mock mvc를 사용할 수 있게 준비해 준다. 아래 코드 참조
// @WebMvcTest는 Slice 테스트이다. 컨트롤러외의 빈들을 로드하지 않는다. 불필요하다고 판단한 것을을 로드하지 않는다.
// 그래서 아래 테스트 함수는 실패한다.
//@WebMvcTest
// 이를 해결하기 위해서 Integration 테스트를 한다. 이를 위해 @WebMvcTest 대신 @SpringBootTest 어노테이션을 사용한다.
// 이 경우 Mock MVC의 존재를 모르므로, @AutoConfigurationMockMvc도 추가한다.
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Data REST 테스트")
// @Transactional을 추가하면, 테스트에서 동작하는 트랜잭션의 기본 동작은 rollback이 된다.(각각의 unit test들은 rollback과 묶이게 된다)
@Transactional
//TODO - 테스트가 제대로 돌려면 SecurityConfig 설정을 임포트해야 한다. 타 테스트파일 참조
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
                //.andDo(print());
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 댓글 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 회원 관련 API는 일체 제공하지 않는다.")
    @Test
    void givenNothing_whenRequestingAccounts_thenThrowsException() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(post("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(put("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(patch("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(head("/api/userAccounts")).andExpect(status().isNotFound());
    }
}
