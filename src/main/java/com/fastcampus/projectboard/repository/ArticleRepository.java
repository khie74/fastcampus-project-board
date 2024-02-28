package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        // querydsl 사용
        // Article Entity안에 있는 모든 필드에 대한 기본 검색 기능을 추가해 준다.
        QuerydslPredicateExecutor<Article>,
        // 검색에 대한 세부적인 규칙을 정할 수 있다.(이 인터페이스에는 customize 메소드만 달랑 있다)
        QuerydslBinderCustomizer<QArticle>
{

    // Containing을 붙이면 부분 일치도 검색에 걸린다.
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    // Article이 들고 있는 UserAccount의 id로 검색을 해야 하므로 아래처럼 작성해야 한다.
    Page<Article> findByUserAccount_IdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    //주의! 해시태그는 일종의 분류이므로, 정확하게 맞는 검우만 검색에 걸리게 하였다.
    // 즉, 여기선 Containing을 넣지 않음.
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    // Java 8 이후로 인터페이스 구현을 넣을 수 있게 되었다.
    // 인터페이스에 메소드 구현을 넣을 떄는 default method로 넣어야 한다.

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // 선택적인 필드만을 검색에 사용하고 싶다.
        // 제목, 본문, id, 닉네임, 해시태그

        // 리스트하지 않은 필드는 검색에서 제외하도록 한다.
        bindings.excludeUnlistedProperties(true);
        // 검색에 포함시킬 필드들을 지정
        //
        // '본문'은 데이터양이 크기 때문에 원래 검색 필드에 넣을게 아니다.
        // 보통은 다른 서비스를 사용해서 해결하곤 하는데...
        // 우리는 공부가 목적이니 본문도 검색 대상에 포함시켰다.
        //
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        // exact 매칭이 아니라, 부문 매칭을 사용하도록 변경
        // 검색 파라미터는 하나만 받기 때문에 first를 사용
        // StringExpression:likeIgnoreCase // like '${}' 이런 쿼리가 생성된다. 즉, 사용자가 %를 직접 넣어주어야 한다.
        // StringExpression:containsIgnoreCase // like '%${v}%' 이런 쿼리가 생성된다.

        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        // 날짜 타입이므로...
        // 현재 시분초까지 일치시켜야만 하는데, 이는 나중에 개선할 예정
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
