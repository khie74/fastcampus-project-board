package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    // primary key 지정
    @Id
    // 자동 증가되도록 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String userId;
    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter
    @Column(length = 100)
    private String email;
    @Setter
    @Column(length = 100)
    private String nickname;
    @Setter
    private String memo;

    // Hibernate를 사용하는 Entity 구현체의 경우, 기본 생성자를 가져야 한다.
    // 아무 인자가 없어야 하고, public 또는 protected로 만들기
    protected UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }

    // @Entity의 경우에는 따로 구현해 주어야 한다.
    // 중요! 영속화하지 않은 것들은 동등성 검사에서 false로 판정해야 한다.
    //
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // 자바 14에서 추가된 문법
        if (!(o instanceof UserAccount that)) return false;

        // 주의! 영속화하지 않은 것들은 동등성 검사에서 false로 판정해야 한다.
        // 이런 점 때문에 IDE가 구현해 준 코드를 그대로 사용해서는 안된다.
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
