package com.web.swagger.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = {"com.web.controllers"})// 扫描路径
public class Swagger2Config implements WebMvcConfigurer {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.securityContexts(securityContexts)
				.securitySchemes(authKey)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();

	}

	List<SecurityContext> securityContexts = Collections.singletonList(SecurityContext.builder()
			.securityReferences(Collections.singletonList(SecurityReference.builder()
					.reference("Authorization")
					.scopes(new AuthorizationScope[]{new AuthorizationScope("global",
							"accessEverything")}).build()))
			.build());
	List<SecurityScheme> authKey = Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));




	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("JavaWebFinal API")
				.description("NXY666_NB_API")
				.termsOfServiceUrl("http://localhost:8080/")
				.version("99.0.0")
				.build();
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
	}

}