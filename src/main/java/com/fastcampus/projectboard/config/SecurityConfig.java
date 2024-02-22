package com.fastcampus.projectboard.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

// 스프링부트 2x에서는 WebSecurityConfigurerAdapter를 상속 받았으나,
// 3.x에서는 Bean 컴포넌트 방식으로 변경되었다.
//
@Configuration
public class SecurityConfig {
    // Security Filter Chain을 Bean으로 등록해서 작성해야 한다.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 일단 어떤 요청이든 허용하겠다라고 설정
        http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                // 로그인 페이지를 만들도록 설정
                .formLogin((formLogin) -> {
                    //설정들이 들어가야 한다!!!

                    formLogin.defaultSuccessUrl("/articles", true);
                });
        return http.build();
    }
}
