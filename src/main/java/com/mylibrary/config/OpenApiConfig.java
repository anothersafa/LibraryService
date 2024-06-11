package com.mylibrary.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info();
        info.title("Library System API Documentation");
        info.description("This is a documentation of API developed for Library System");
        info.version("2.0");

        List<Server> servers = new ArrayList<>();

        Server server = new Server();
        server.description("Localhost");
        server.url("http://localhost:9191");
        servers.add(server);

        return new OpenAPI().components(new Components()).info(info).servers(servers);
    }

    @Bean
    public GroupedOpenApi v1ApiGroup() {
        String paths[] = { "/**" };
        String pathsToExclude[] = { "/v2/**", "/" };
        return GroupedOpenApi.builder().group("~v1.0").pathsToMatch(paths)
                .pathsToExclude(pathsToExclude)
                .displayName("V 1.0.0")
                .build();
    }
}
