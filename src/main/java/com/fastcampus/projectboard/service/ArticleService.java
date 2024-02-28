package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // 롬복 기능을 사용해서 간편하게 로깅을 사용할 수 있다.
@Service
// 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 어노테이션
@RequiredArgsConstructor
// 트랜젝션을 적용하기 위해서...
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    //
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_IdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            //TODO - 해시태그 검색시, 사용자가 #을 넣는 경우와 넣지 않는 경우가 있다. 두 경우 모두 검색되도록 리펙토링 필요함.
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                // JPA에서 제공하는 EntityNotFoundException 예외를 사용하자.
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId: " + articleId));
    }

//    @Transactional(readOnly = true)
//    public ArticleDto searchArticle(long l) {
//        return null;
//    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            // 주의! save()를 호출하지 않고 있다.(필요없다!!!)
            // class level transactional에 의해서 메소드 단위로 트랜젝션이 묶여있다.
            // 그래서 트랜젝션이 끝날 때 영속성 컨텍스트는 article이 변한 것을 감지해서
            // 쿼리를 날려서 저장한다.
            // 그래서 save()를 명시할 필요가 없다.(명시해도 상관없다)

            Article article = articleRepository.getReferenceById(dto.id());
            if(dto.title() != null) article.setTitle(dto.title());
            if(dto.content() != null) article.setContent(dto.content());
            article.setHashtag(dto.hashtag());
        } catch(EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. date: {}", dto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
