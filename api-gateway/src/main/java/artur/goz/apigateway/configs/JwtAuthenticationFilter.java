package artur.goz.apigateway.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
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

                log.debug(username);
                log.debug(roles);

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Name", username)
                        .header("X-Roles", roles)
                        .build();

                // Оновити exchange з новим запитом
                exchange = exchange.mutate().request(mutatedRequest).build();
            }
            return chain.filter(exchange);
        }
    }

