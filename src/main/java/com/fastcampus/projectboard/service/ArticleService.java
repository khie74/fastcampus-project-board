package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return null;
    }

//    @Transactional(readOnly = true)
//    public ArticleDto searchArticle(long l) {
//        return null;
//    }

    public void saveArticle(ArticleDto dto) {

    }

    public void updateArticle(ArticleDto dto) {

    }

    public void deleteArticle(long articleId) {
    }
}
