package jiyeon.travel.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
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
        return new Info()
                .title("My Travel Destination API")
                .description("[자세한 내용은 GitHub README에서 확인할 수 있습니다.](https://github.com/jiyeon0926/my-travel-destination)");
    }
}
