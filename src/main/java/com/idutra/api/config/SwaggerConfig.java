package com.idutra.api.config;

import com.idutra.api.component.MensagemComponente;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.idutra.api.constants.MensagemConstant.DOC_TAG_CHAR;

@Profile({"dev", "test"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final MensagemComponente mensagemComponente;

    @Autowired
    public SwaggerConfig(MensagemComponente mensagemComponente) {
        this.mensagemComponente = mensagemComponente;
    }

    @Value("${app.build.version}")
    private String appBuildVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.idutra.api.rest.controller"))
                .paths(PathSelectors.any()).build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(this.mensagemComponente.get("swagger.title"))
                .description(this.mensagemComponente.get("swagger.description"))
                .version(appBuildVersion)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(this.mensagemComponente.get("swagger.title"))
                        .version(this.appBuildVersion)
                        .contact(new Contact().name("Igor Dutra").email("igor.p.dutra87@gmail.com").url("https://github.com/idutra"))
                        .description("Esta API tem por objetivo prover interfaces de comunicação REST para inserir, atualizar, excluir e consultar personagens do mundo de Harry Potter" +
                                "\nEsta API faz integração com o [PotterApi] (https://www.potterapi.com/) para validar as informações inseridas")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addTagsItem(new Tag().name(DOC_TAG_CHAR).description("Gerenciamento de Personagens. " +
                        "\nEste tópico tem por objetivo manter um personagem da API em uma base de dados interno."));
    }
}
