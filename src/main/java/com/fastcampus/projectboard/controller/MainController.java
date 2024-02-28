package com.fastcampus.projectboard.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        // 게시판 화면으로 리다이렉트
        //return "redirect:/articles";
        // 포워드로 변경
        return "forward:/articles";
    }
}
