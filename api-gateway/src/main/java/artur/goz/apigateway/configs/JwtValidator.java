package artur.goz.apigateway.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JwtValidator {

    @Value("${jwtSecret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }


    public String getUsernameFromJWT(String token) {
        Claims claims = getClaimsFromJWT(token);
        return claims.getSubject();
    }

    public String getRolesFromJWT(String token) {
        Claims claims = getClaimsFromJWT(token);
        return claims.get("roles", String.class);
    }

    public Claims getClaimsFromJWT(String token) {
        SecretKey key = getSigningKey();

        return Jwts.parserBuilder() // Використання нового методу
                .setSigningKey(key) // Передача об'єкта ключа
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJWT(String token) {
        try {
            Jwts.parserBuilder() // Використання нового API
                    .setSigningKey(getSigningKey()) // Передача ключа
                    .build() // Побудова парсера
                    .parseClaimsJws(token); // Верифікація токена
            return true;
        } catch (Exception e) {
            log.warn("{} invalid JWT token ", e.getMessage());
            return false;
        }
    }
}

