package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 어노테이션
@Transactional // 트렌젹션을 적용하기 위해서...
@Service
public class ArticleCommentService {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
//    private Optional<Article> byId;

    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        // 내가 대충 만듦.
        // 구현이 없기래...
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream().map(it -> ArticleCommentDto.of(
                        it.getId(),
                        UserAccountDto.of(
                                it.getUserAccount().getId(),
                                it.getUserAccount().getUserId(),
                                it.getUserAccount().getUserPassword(),
                                it.getUserAccount().getEmail(),
                                it.getUserAccount().getNickname(),
                                it.getUserAccount().getMemo()
                        ),
                        it.getContent(),
                        it.getCreatedAt(),
                        it.getCreatedBy(),
                        it.getModifiedAt(),
                        it.getModifiedBy()
                )).toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {

    }

    public void updateArticleComment(ArticleCommentDto dto) {

    }

    public void deleteArticleComment(Long articleCommentId) {

    }
}
