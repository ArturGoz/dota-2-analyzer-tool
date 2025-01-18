package artur.goz.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTGenerator {
    @Value("${jwtSecret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateJWT(String username, String roles) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(username) // Ім'я користувача
                .claim("roles", roles) // Додавання ролей до claims
                .setIssuedAt(new Date()) // Дата створення токена
                .setExpiration(new Date(System.currentTimeMillis() + 864_000)) // Час дії токена (10 хвилин)
                .signWith(key) // Підпис
                .compact();
    }
}



