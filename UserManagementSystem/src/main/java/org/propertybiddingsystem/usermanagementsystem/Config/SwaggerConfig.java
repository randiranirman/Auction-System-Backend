package org.propertybiddingsystem.usermanagementsystem.Config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {

    @Bean

    public OpenAPI UserManagementAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("This  is api for authentication ")
                .version("1.0.0")
                .description("auth API for now "))
  ;


    }
}
