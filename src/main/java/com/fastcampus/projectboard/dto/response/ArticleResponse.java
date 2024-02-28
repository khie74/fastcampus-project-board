package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j // 로그 볼려고 그냥 추가
public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static ArticleResponse from(ArticleDto dto) {
        //sks temp
        //log.info("정상적으로 생성일이 들어있는지를 체크: {}", dto.createdAt());


        String nickname = dto.userAccountDto().nickname();
        // 닉네임이 없는 경우, ID를 사용해라...
        if(nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }
        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
