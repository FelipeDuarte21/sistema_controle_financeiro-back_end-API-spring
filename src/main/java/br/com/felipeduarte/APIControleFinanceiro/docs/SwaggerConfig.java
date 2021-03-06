package br.com.felipeduarte.APIControleFinanceiro.docs;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.felipeduarte.APIControleFinanceiro.resource"))
          .paths(PathSelectors.ant("/**"))
          .build()
          .globalOperationParameters(Arrays.asList(
        		  new ParameterBuilder()
        		  .name("Authorization")
        		  .description("Token de acesso")
        		  .modelRef(new ModelRef("string"))
        		  .parameterType("header")
        		  .required(false)
        		  .build()
           ))
          .apiInfo(metaData());
    }
	
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("API de Controle Financeiro")
				.description("API backend do projeto controle financeiro")
				.version("1.0")
				.license("Licença Pública Geral GNU")
				.licenseUrl("https://www.gnu.org/licenses/gpl-3.0.html")
				.contact(new Contact("Felipe Duarte", "", "felipe15lfde@gmail.com"))
				.build();
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
		.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
		.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }
	
}
