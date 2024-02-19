package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// DataJpaTest의 지원을 받겠다.
// 이 기능을 사용하면 테스트를 돌릴 때 자동으로 트렌젝션이 걸린다.
//
//
// DataJpaTest가 가동하는 순간, test auto configuration에서
// 모든 데이터베이스 설정을 무시하고, 테스트용 db가 미리 지정해 놓은 db를 띄워버린다.
// 자동으로 테스트 db를 띄우지 못하게 하려면
// 아래 어노테이션을 추가해주면 된다.
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 이렇게 하면 테스트 실행시 테스트 db를 불러오지 않고, 설정되어 있는 것을 사용한다.
//
@DataJpaTest
// 내가 만든 JpaConfig 정보를 이 테스트 코드는 모를테니, 알려주어야 한다.
@Import(JpaConfig.class)
@DisplayName("JPA 연결 테스트")
// application.yaml파일에 정의한 "testdb" 프로파일을 활성화한다.
//@ActiveProfiles("testdb")
class JpaRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(JpaRepositoryTest.class);


    // 생성자 주입 패턴을 사용(JUnit 5와 최신 Spring을 사용하는 경우, 사용 가능)
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    public JpaRepositoryTest(
            // 생성자 주입 패턴
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("Select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles)
                .isNotNull();
        assertThat(articles.size())
                .isEqualTo(3);
    }

    @DisplayName("Insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();;

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);
    }


    @DisplayName("Update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {

        // 주의!
        // 테스트를 돌릴 때 트렌젝션은 기본적으로 rollback으로 동작한다.
        // 그러므로 어차피 롤백될 데이터를 update하려고 해도 실행되지 않는다.
        // 이 문제를 해결하려면 flush를 해주어야 한다.

        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        assertThat(savedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("Delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        // 댓글도 삭제되는지를 확인한다.캐스캐이딩이 ALL로 되어 있으므로 함께 삭제되어야 한다.
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        log.info("삭제 전 글 개수: {}, 댓글 개수: {}", previousArticleCount, previousArticleCommentCount);

        // When
        articleRepository.delete(article);

        log.info("삭제 후 글 개수: {}, 댓글 개수: {}", articleRepository.count(), articleCommentRepository.count());


        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }
}
