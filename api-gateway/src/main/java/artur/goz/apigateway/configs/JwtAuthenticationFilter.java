package artur.goz.apigateway.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {
        @Autowired
        private JwtValidator jwtValidator;

        @Autowired
         private RouterValidator validator;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            if (validator.isSecured.test(request)) {
                String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (token == null || !token.startsWith("Bearer ")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                token = token.substring(7); // Видалити "Bearer "

                if (!jwtValidator.validateJWT(token)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                // Додати інформацію про користувача в заголовок
                String username = jwtValidator.getUsernameFromJWT(token);
                String roles = jwtValidator.getRolesFromJWT(token);
                exchange.getRequest().mutate()
                        .header("X-User-Name", username)
                        .header("X-Roles", roles)
                        .build();
            }
            return chain.filter(exchange);
        }
    }

