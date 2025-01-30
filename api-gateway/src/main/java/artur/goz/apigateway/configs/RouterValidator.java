package artur.goz.apigateway.configs;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    //permitAll requests
    public static final List<String> openEndpoints = List.of(
            "/auth/",
            "/page/",
            "/css/","/js/","/miniheroes/","/images/","/roles/",
            "/tournament/"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
