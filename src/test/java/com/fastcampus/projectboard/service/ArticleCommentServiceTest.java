package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicIntegerFieldUpdater;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
public class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService sut;
    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private ArticleRepository articleRepository;

    Logger log = LoggerFactory.getLogger(ArticleCommentServiceTest.class);


    @DisplayName("게시글 ID로 조회하면 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingComments_thenReturnsComments() {
        // Given
        Long articleId = 1L;
        //내가 맘대로 맘든 사용자
        //UserAccount userAccount = UserAccount.of("sks", "pw", null, null, null);

        ArticleComment expected = createArticleComment("content");

//        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(Optional.of(
//                Article.of(userAccount, "title", "content", "#java")
//        ));

        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

        List<ArticleComment> comments = articleCommentRepository.findByArticle_Id(articleId);
        log.info("댓글 조회 count={}", comments.size());

        // When
        //List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);

        //sks temp
        log.info("실제로 목록을 가져왔을까? count={}", actual.size());

        // Then
        //assertThat(articleComments).isNotNull();
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        //then(articleRepository).should().findById(articleId);
        // 왜 이걸 넣으면 테스트가 실패하지???
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    // 여기서 부터는 강의에 소개되지 않음.
    // ArticleServiceTest를 보고서 동일하게 작성하였음.

    @DisplayName("댓글 정보를 입력하면 댓글을 저장한다.")
    @Test
    void givenArticleId_whenSavingComment_thenSavingComment() {
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(ArticleCommentDto.of(1L, createUserAccountDto(), "hello comment", LocalDateTime.now(), "sks", LocalDateTime.now(), "sks"));

        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    //TODO - update 테스트

    //TODO - delete 테스트





    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                createUserAccountDto(),
                "content",
                LocalDateTime.now(),
                "sks",
                LocalDateTime.now(),
                "sks"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
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

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                Article.of(createUserAccount(), "title", "content", "hashtag"),
                    createUserAccount(),
                content
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "newSks",
                "password",
                "yyy@gmail.com",
                "newSks",
                "newSks memo"
        );
    }
}
