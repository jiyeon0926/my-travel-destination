package jiyeon.travel.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement()
                        .addList("jwt"))
                .components(new Components()
                        .addSecuritySchemes("jwt", new SecurityScheme()
                                .name("jwt")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ));
    }

    private Info apiInfo() {
        Contact contact = new Contact();
        contact.name("김지연")
                .url("https://github.com/jiyeon0926")
                .email("wldus2499@naver.com");

        return new Info()
                .title("My Travel Destination API")
                .summary("사용자들이 작성한 여행 블로그를 한 곳에서 확인하고, 나의 다음 여행지를 발견하는 서비스")
                .description("GitHub: <a href='https://github.com/jiyeon0926/my-travel-destination'></a>")
                .contact(contact);
    }
}
