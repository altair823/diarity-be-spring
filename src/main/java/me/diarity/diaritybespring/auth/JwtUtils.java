package me.diarity.diaritybespring.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Setter
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey(String secret) {
        byte[] keyByte = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public String generateToken(
            String username,
            String role,
            String email,
            long expiration
    ) {
        Claims claims = Jwts.claims()
                .add("username", username)
                .add("role", role)
                .add("email", email)
                .build();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getKey(secret))
                .compact();
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey(secret))
                .build()
                .parseSignedClaims(token);
    }

    public String getUsername(String token) {
        return getClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).getPayload().get("role", String.class);
    }

    public String getEmail(String token) {
        return getClaims(token).getPayload().get("email", String.class);
    }
}
