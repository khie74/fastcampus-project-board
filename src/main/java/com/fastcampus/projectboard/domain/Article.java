package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
// AuditingFields까지 toString오로 찍기 위해서 callSuper=true 옵션을 추가
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),

        // auditing field들은 아쉽게도 별도의 클래스로 분리 불가
        // 즉, AuditingFields 클래스로 이동 불가

        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

//@EntityListeners 어노테이션은 AuditingFields로 이동
//// 엔티티에서도 auditing을 쓴다는 것을 명시해야 한다.
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article extends AuditingFields {
    // primary key로 지정
    @Id
    // 자동으로 증가하게 설정
    // MySQL의 경우, auto increment는 IDENTITY 방식으로 만들어진다.
    // 그래서 MySQL의 경우에는 이렇게 설정해야 한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보(ID)


    // 사용자가 특정 필드값을 변경할 수 없도록 일부 필드에만 @Setter를 건다.
    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문
    @Setter private String hashtag; // 해시태그

    // article 테이블로 부터 온 것임을 mappedBy로 명시한다.
    // 공부 목적으로 양방향 바인딩을 하고 있다.
    // 실무에서는 이러한 양방향 바인딩을 푸는 경우가 많다.
    // 데이터마이그레이션하거나, 데이터가 유실되거나, admin에서 데이터를 편집하고자 할 때 불편하기 떄문이다.
    // 예를 들어 게시글을 삭제하면, 딸린 댓글이 사라지는게 맞지만,
    // 운영에서는 그렇지 않을 수 있다. 백업 목적으로 남기고 싶을 수 있기 떄문이다.
    // 또한 성능을 위해서도 foreign key를 걸지 않느 경우도 많다.
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    //@OrderBy("id")
    @OrderBy("createdAt DESC")
    // toString에서 순환참조가 발생할 수 있으므로, toString대상에서 제외 시킨다.
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    /*
    이 내용은 AuditingFIelds 클래스로 이동시킴.

    // 아래 필드들은 JPA에서 자동으로 세팅되도록 설정
    // 아래처럼 어노테이션을 붙여주는 것만으로 auditing이 자동으로 동작한다.
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시
    // 이 정보의 경우, JPA가 어떤 정보를 넣어야 할지를 알지 못한다.
    // 이를 해결하기 위해서 설정 파일(JpaConfig)에서 추가 설정이 필요하다.
    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; // 생성자
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자
     */

    // Entity로써의 기본기능을 만족시키기 위한 내용을 추가

    // Hibernate를 사용하는 Entity구현체의 경우, 기본생성자를 가져야 한다.
    // 아무 인자 없어야 하고, public 또는 protected로 만들기
    protected Article() {}

    // 메타 데이터는 제외하고, 실제로 입력받는 필드만을 받는 생성자를 만든다.
    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    // 동일성, 동등성 검사를 위한 equals, hashCode를 만들어야 한다.
    // 롬복의 @EqualsAndHashCode를 사용하게 되면, 기본적인 방식으로 만들어진다.
    // entitiy에서는 다른 방식으로 만들어주어야 한다.


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // 'article'이 바로 pattern variable이다.
        // 자바 14에서 추가된 기능
        if (!(o instanceof Article that)) return false;
        // 위에서 article pattern variable을 가지고 있으므로, 바로 아래에서 사용하였다.

        // 주의!!!
        // 아직 영속화되지 않은 것들은 등동성 검사에서 false를 리턴하도록 한다!!!
        // 이런 점 때문에 직접 구현해 주는게 맞는것 같다. Objects.equals를 쓰면 안된다.
        // (null인 경우, 판정이 다르므로...)
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
