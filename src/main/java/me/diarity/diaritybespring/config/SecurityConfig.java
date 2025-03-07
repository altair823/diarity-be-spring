package me.diarity.diaritybespring.config;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.auth.JwtFilter;
import me.diarity.diaritybespring.auth.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(
                            java.util.List.of(
                                    "http://localhost:3000",
                                    "https://dev.diarity.me",
                                    "https://diarity.me"
                            )
                    );
                    corsConfiguration.setAllowedMethods(java.util.List.of("*"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/index",
                                "/auth/login/google",
                                "/auth/login/google/callback",
                                "/auth/login/google/withaccesstoken",
                                "/auth/status",
                                "/posts",
                                "/posts/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
