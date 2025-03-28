package me.diarity.diaritybespring.auth;

import io.jsonwebtoken.ExpiredJwtException;
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
        String token = jwtUtils.generateToken(username, role, email, 1000 * 60 * 60);
        assertEquals(username, jwtUtils.getUsername(token));
        assertEquals(role, jwtUtils.getRole(token));
        assertEquals(email, jwtUtils.getEmail(token));
    }

    @Test
    public void generateTokenSignatureFail() {
        String token = jwtUtils.generateToken(username, role, email, 1000 * 60 * 60);
        token += "abcd";
        String finalToken = token;
        assertThrows(SignatureException.class, () -> jwtUtils.getUsername(finalToken));
    }

    @Test
    public void generateTokenExpiredFail() {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecret("testSecrettestSecrettestSecrettestSecrettestSecrettestSecret");
        String token = jwtUtils.generateToken(username, role, email, -1);
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.getUsername(token));
    }
}
