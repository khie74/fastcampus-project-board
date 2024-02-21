package com.fastcampus.projectboard.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {

        // 아래 설정한 프로퍼티 필드를 가지고, 설정을 받아서 세팅읊 하겠다라는 의미
        // 이 코드는 application.yaml의 설정값을 받기 위한 것으로
        // 이게 귀찮다면 그냥
        // defaultTemplateResolver.setUseDecoupledLogic(true);
        // 라고 넣어주면 된다.
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }

    @RequiredArgsConstructor
    @Getter
    // 원래 강사님 소스에는 있었는데, 스프링 부트가 3.대로 올라오면서
    // @ConstructorBinding이 필요없어졌다.(생성자가 여러 개일 때는 제외)
    // 빌드가 에러나서 코멘트 아웃처리하였다.
    //@ConstructorBinding
    // 내가 만든 프로퍼티 이름을 thymeleaf3라고 함. application.yaml에서 사용하는 코드 참조
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {

        // 여기에서 프로퍼티 필드를 하나 뚫은거다.

        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;
    }
}
