package artur.goz.apigateway.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("static-css", r -> r.path("/css/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("static-images", r -> r.path("/images/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("static-js", r -> r.path("/js/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("static-miniheroes", r -> r.path("/miniheroes/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("static-roles", r -> r.path("/roles/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("auth-front-service", r -> r.path("/page/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("getter-front-service", r -> r.path("/get/**")
                        .uri("lb://FRONT-SERVICE"))
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("game", r -> r.path("/analyze/**")
                        .uri("lb://WINNER-CALCULATOR"))
                .route("user-service", r -> r.path("/profile/**")
                        .uri("lb://USER-SERVICE"))
                .build();
    }

}
