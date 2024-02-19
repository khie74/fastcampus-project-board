package com.fastcampus.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// 공통 필드를 별도의 클래스로 빼는 경우, @EntityListeners도 함께 옮겨지는 구나.
// 엔티티에서도 auditing을 쓴다는 것을 명시해야 한다.
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@ToString
@Getter
public class AuditingFields {
    // 웹화면에 표시할 때 파싱을 위한 룰을 넣어준다.
    // Spring Framework formmater 어노테이션중에 @DateTimeFormat이 있다.
    // 이걸 통해서 할 수 있다.
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false /*이 필드는 업데이트 불가*/) private LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100, updatable = false/*이 필드는 업데이트 불가*/) private String createdBy; // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
}
