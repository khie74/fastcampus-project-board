package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

// 컨트롤러에서만 사용

public record ArticleCommentResponses(
    Long id,
    String content,
    LocalDateTime createdAt,
    String email,
    String nickname
) implements Serializable {

    public static ArticleCommentResponses of(Long id, String content, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleCommentResponses(id, content, createdAt, email, nickname);
    }

    public static ArticleCommentResponses from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        // 닉네임이 없으면, 사용자 ID를 사용한다.
        if(nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();;
        }
        return new ArticleCommentResponses(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
