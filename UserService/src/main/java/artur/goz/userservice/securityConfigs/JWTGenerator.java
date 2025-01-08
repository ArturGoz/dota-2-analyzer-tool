package artur.goz.userservice.securityConfigs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
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

    public String generateJWT(Authentication authentication) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000)) // 10 днів
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        SecretKey key = getSigningKey();
        Claims claims = Jwts.parserBuilder() // Використання нового методу
                .setSigningKey(key) // Передача об'єкта ключа
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateJWT(String token) {
        try {
            Jwts.parserBuilder() // Використання нового API
                    .setSigningKey(getSigningKey()) // Передача ключа
                    .build() // Побудова парсера
                    .parseClaimsJws(token); // Верифікація токена
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT");
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}
