package com.example.talktudy.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    TypeResolver typeResolver = new TypeResolver();

    @Bean
    public Docket api() {
        // 헤더에 Authorization 토큰 추가하기
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("Authorization")
                .description("Access Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();


        List<Parameter> globalParameters = new ArrayList<>();
        globalParameters.add(parameterBuilder.build());

        return new Docket(DocumentationType.OAS_30)
                .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(CustomPageable.class)))
                .useDefaultResponseMessages(false) // 기본 응답 코드 노출 여부
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.talktudy.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Talktudy Swagger")
                .description("톡터디 REST API Swagger")
                .version("1.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    // 스웨거 설정을 위한 CustomPageable 모델
    @Data
    @ApiModel
    static class CustomPageable {

        @ApiModelProperty(value = "페이지 번호(0 ~ N)")
        private Integer page;

        @ApiModelProperty(value = "페이지 크기(4, 8..)")
        private Integer size;

//        @ApiModelProperty(value = "정렬(사용법 : 컬럼명,ASC|DESC")
//        private List<String> sort;
    }
}