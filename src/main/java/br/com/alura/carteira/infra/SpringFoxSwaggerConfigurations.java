package br.com.alura.carteira.infra;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration //para o spring saber que essa é uma classe apenas de configuracao
public class SpringFoxSwaggerConfigurations {
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build()
          .globalRequestParameters(Arrays.asList( //para deixar o campo de inserir token nas outras requests disponivel no swagger
        		  new RequestParameterBuilder()
        		  .name("Authorization")
        		  .description("Bearer Token")
        		  .required(false)
        		  .in("header")
        		  .build()));                                           
    }

}
