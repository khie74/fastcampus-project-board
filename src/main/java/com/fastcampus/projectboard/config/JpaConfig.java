package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;


// Java클래스 형태를 사용해서 JPA 설정을 하고 있다.
@Configuration
// JPA Auditing 기능을 활성화
@EnableJpaAuditing
public class JpaConfig {

    // auditing 기능에서 사람 이름이 필요한 경우 무조건 이 이름을 사용하게 된다.
    // createdBy, modifiedBy를 넣는 경우, 사용된다.
    // 이 코드는 현재 하드코딩으로 추후 SpringSecurity로 변경되어야 한다.
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("sks"); //TODO: 스프링 시큐리티로 인증기능을 붙이게 될 때 수정하자!!!
    }
}
