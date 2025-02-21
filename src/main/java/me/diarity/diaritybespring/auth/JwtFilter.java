package me.diarity.diaritybespring.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private static final String[] excludePathPatterns = {
            "/index",
            "/auth/login/google",
            "/auth/login/google/callback"
    };

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            token = token.substring(7);
            Jws<Claims> claims = jwtUtils.getClaims(token);
            String username = (String) claims.getPayload().get("username");
            String role = (String) claims.getPayload().get("role");
            String email = (String) claims.getPayload().get("email");
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(
                    new SimpleGrantedAuthority(role)
            ));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            System.out.println("Token is null or does not start with Bearer");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
        for (String pattern : excludePathPatterns) {
            if (request.getRequestURI().contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
