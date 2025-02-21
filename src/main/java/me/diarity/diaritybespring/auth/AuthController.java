package me.diarity.diaritybespring.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.auth.dto.LoginUserInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/google")
    public String googleLogin() {
        return authService.getGoogleLoginUrl();
    }

    @GetMapping("/login/google/callback")
    public LoginUserInfoResponse googleLoginCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        return authService.googleLoginCallback(code);
    }
}
