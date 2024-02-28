package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long /*id 타입*/>,
        QuerydslPredicateExecutor<ArticleComment>,
    QuerydslBinderCustomizer<QArticleComment>
{

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 리스트하지 않은 필드는 검색에서 제외하도록 한다.
        bindings.excludeUnlistedProperties(true);
        // 검색에 포함시킬 필드들을 지정
        bindings.including(root.content, root.createdAt, root.createdBy);
        // exact 매칭이 아니라, 부문 매칭을 사용하도록 변경
        // 검색 파라미터는 하나만 받기 때문에 first를 사용
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        // 날짜 타입이므로...
        // 현재 시분초까지 일치시켜야만 하는데, 이는 나중에 개선할 예정
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }


    // 게시글 ID를 통해서 댓글을 검색한다.
    // 이름에서 연관관계를 써주고, 언더바를 써주면 그 연관관계 객체로 들어간다는 의미
    // 그러므로 언더버 뒤에 붙은 id는 게시글의 id가 된다.
    List<ArticleComment> findByArticle_Id(Long articleId);

}
