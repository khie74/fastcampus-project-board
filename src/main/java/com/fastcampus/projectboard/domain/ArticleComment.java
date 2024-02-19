package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@ToString
@Table(indexes = {
    @Index(columnList = "content"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
//@EntityListeners 어노테이션은 AuditingFields로 이동
//// 엔티티에서도 auditing을 쓴다는 것을 명시해야 한다.
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 연관관계를 구성하지 않는다면 이렇게 하겠지.
    //private Long articleId;
    // 그런데 우리는 객체지향적으로 연관관계를 줄 것이다.
    // 필수값이므로 optional = false
    // 캐스케이딩 옵션
    // 댓글을 변경하거나 지웠을 때 관련된 계시물에 영향을 미치지 않는다.
    // 그러므로 캐스캐이딩 옵션은 주지 않는다.
    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문

    /*
    이 내용은 AuditingFields 클래스로 이동
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
    */

    // 롬복으로도 할 수 있다. @NoArgsConstructor(access = ...)
    // 어차피 한 줄이어서 그냥 씀.
    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
