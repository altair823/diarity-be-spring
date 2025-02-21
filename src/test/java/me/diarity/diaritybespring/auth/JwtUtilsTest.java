package me.diarity.diaritybespring.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;

    private final String username = "testUser";
    private final String role = "NORMAL";
    private final String email = "testemail@gmail.com";

    @Test
    public void generateTokenSuccess() {
        String token = jwtUtils.generateToken(username, role, email);
        Jws<Claims> claims = jwtUtils.getClaims(token);
        assertEquals(username, claims.getPayload().get("username"));
        assertEquals(role, claims.getPayload().get("role"));
        assertEquals(email, claims.getPayload().get("email"));
    }

    @Test
    public void generateTokenSignatureFail() {
        String token = jwtUtils.generateToken(username, role, email);
        token += "abcd";
        String finalToken = token;
        assertThrows(SignatureException.class, () -> jwtUtils.getClaims(finalToken));
    }

    @Test
    public void generateTokenExpiredFail() {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecret("testSecrettestSecrettestSecrettestSecrettestSecrettestSecret");
        jwtUtils.setExpiration(-1000);
        String token = jwtUtils.generateToken(username, role, email);
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.getClaims(token));
    }
}
