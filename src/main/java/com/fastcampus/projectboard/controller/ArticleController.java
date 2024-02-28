package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//롬복, 필수 파라미터를 가진 생성자를 만들어준다.
@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    // @RequiredArgsConstructor로 컨스트럭트 인젝션을 하였다.
    private final ArticleService articleService;

    @GetMapping
    public String articles(
            // @RequestParam으로 GET 파라미터를 가져온다.
            // 없으면 전체 게시글 조회할꺼라 required를 false로 설정
            @RequestParam(required = false) SearchType searchType,
            // @RequestParam으로 GET 파라미터를 가져온다.
            // 없으면 전체 게시글 조회할꺼라 required를 false로 설정
            @RequestParam(required = false) String searchValue,
            //@PageableDefault를 명시하지 않아도 동작하지만..
            // 이 어노테이션으로 기본동작을 추가할 수 있다. 명시의 차원에서 넣어줌.
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        map.addAttribute("articles",
                // articleService.searchArticles가 이미 DTO를 리턴하지만
                // 응답용 정보만 담은 별도의 DTO를 사용한다.
                articleService.searchArticles(searchType, searchValue, pageable)
                        .map(ArticleResponse::from)

        );
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(
            @PathVariable Long articleId,
            ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponses());
        return "articles/detail";
    }
}
