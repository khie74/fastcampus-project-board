package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

// 스프링 부트의 슬라이스 테스트 기능을 사용하지 않고 작성하는 방법으로 접근함.
// 스프링 부트 애풀리케이션 컨텍스트가 뜨는 시간을 줄이기 위해서
// 그대신 디펜던시가 필요한 경우에는 목킹을 한다. Mockito 활용
//
//
//
@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    // 테스트해야할 클래스
    @InjectMocks // 목을 주입하는 대상.   필드에만 적용 가능한 어노테이션
    private ArticleService sut; // system under test의 약자. 즉, 테스트의 대상을 의미한다.
    @Mock // 필드, 파라미터에 적용 가능한 어노테이션
    private ArticleRepository articleRepository;

//    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
//    @Test
//    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList() {
//        // Given
//
//        // When
//        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그
//
//        // Then
//        assertThat(articles).isNotNull();
//    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitle(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isNotNull();
        assertThat(articles).isEmpty();;
        then(articleRepository).should().findByTitle(searchKeyword, pageable);
    }

//    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
//    @Test
//    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
//        // Given
//
//        // When
//        ArticleDto article = sut.searchArticle(1L);
//
//        // Then
//        assertThat(article).isNotNull();
//    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {

        // 목킹을 이용한 테스트

        //Solitary test
        // 실제로 데이터베이스 레이어단까지 테스트하는 것이 아니라,
        // Mockito를 사용해서 목킹해서 테스트...
        // 유닛테스트

        /*
        참고. 데이터베이스 레이어까지 테스트되는 테스트를 Sociable 테스트라고 한다.
         */

        // 아래 테스트에서는 단지 메서드가 호출되었는지 여부만 확인한다.

        // Given
        // any... Article이면 아무거나 넣어줄 수 있다.
        //아래 코드는 무슨 일이 일어날까더라는 것을 알려주는 역할만 한다.
        // Article 클래스를 전달하면 아무 Article클래스를 반환한다.
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        //TODO - comments는 대충 넣었음...
        sut.saveArticle(ArticleDto.of(1L, createUserAccountDto(), "title", "content", "hashtag",
                LocalDateTime.now(), "sks", LocalDateTime.now(), "sks"));

        // Then
        // 실제로 레포지토리의 save가 호출되었는지를 목킹 검사
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.updateArticle(ArticleDto.of(1L, createUserAccountDto(), "title", "content", "hashtag",
                LocalDateTime.now(), "sks", LocalDateTime.now(), "sks"));

        // Then
        // 실제로 레포지토리의 save가 호출되었는지를 목킹 검사
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        // 아무일도 하지 않는다는 것을 명시
        willDoNothing().given(articleRepository).delete(any(Article.class));

        // When
        sut.deleteArticle(1L);

        // Then
        // 실제로 레포지토리의 save가 호출되었는지를 목킹 검사
        then(articleRepository).should().delete(any(Article.class));
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "sks",
                "pw",
                "abc@gmail.com",
                "sks",
                "This is memo",
                LocalDateTime.now(),
                "sks",
                LocalDateTime.now(),
                "sks"
        );
    }
}
