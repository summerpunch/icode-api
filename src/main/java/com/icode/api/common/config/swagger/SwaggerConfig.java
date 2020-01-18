package com.icode.api.common.config.swagger;

import com.icode.api.common.constant.CustomFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Title: <br>
 * Description: swagger 配置类<br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/21 17:50<br>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(CustomFinal.CUSTOM_PROJECT_SWAGGER_PACKAGE_PATH))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(CustomFinal.CUSTOM_PROJECT_SWAGGER_TITLE)
                .contact(new Contact(CustomFinal.CUSTOM_PROJECT_SWAGGER_AUTHOR, null, CustomFinal.CUSTOM_PROJECT_SWAGGER_MAIL))
                .version(CustomFinal.CUSTOM_PROJECT_SWAGGER_VERSIONS)
                .description(CustomFinal.CUSTOM_PROJECT_SWAGGER_DESC)
                .build();
    }
}