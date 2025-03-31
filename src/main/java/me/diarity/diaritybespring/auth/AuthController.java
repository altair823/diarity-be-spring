package me.diarity.diaritybespring.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.auth.dto.AuthResponse;
import me.diarity.diaritybespring.auth.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${redirect-after-login}")
    private String redirectAfterLogin;

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Value("${jwt.refreshExpiration}")
    private Integer refreshExpiration;

    private final AuthService authService;

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        String googleLoginUrl = authService.getGoogleLoginUrl();
        System.out.println("googleLoginUrl");
        response.sendRedirect(googleLoginUrl);
    }

    @GetMapping("/login/google/callback")
    public void googleLoginCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        JwtResponse jwtResponse = authService.googleLoginCallback(authService.getGoogleAccessToken(code));
        SetCookiesFromJwtResponse(response, jwtResponse);

        response.sendRedirect(redirectAfterLogin);
    }

    @GetMapping("/login/google/withaccesstoken")
    public void googleLoginWithAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = request.getHeader("Authorization");
        JwtResponse jwtResponse = authService.googleLoginCallback(accessToken.split(" ")[1]);
        SetCookiesFromJwtResponse(response, jwtResponse);
    }

    private void SetCookiesFromJwtResponse(HttpServletResponse response, JwtResponse jwtResponse) {
        Cookie accessTokenCookie = new Cookie("access_token", jwtResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(expiration);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setAttribute("SameSite", "None");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", jwtResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setAttribute("SameSite", "None");
        response.addCookie(refreshTokenCookie);

        System.out.println("Access Token Cookie: " + accessTokenCookie.getMaxAge());
        System.out.println("Refresh Token Cookie: " + refreshTokenCookie.getMaxAge());
    }

    @GetMapping("/status")
    public AuthResponse getStatus(HttpServletRequest request) {
        // Find the "access_token" cookie
        if (request.getCookies() == null) {
            throw new RuntimeException("No access token");
        }
        String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No access token"));
        return authService.getStatus(accessToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", "");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}
