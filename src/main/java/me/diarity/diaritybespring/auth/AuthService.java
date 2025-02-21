package me.diarity.diaritybespring.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.auth.dto.GoogleLoginUserInfoResponse;
import me.diarity.diaritybespring.auth.dto.LoginUserInfoResponse;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public String getGoogleLoginUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20profile%20email";
    }

    public LoginUserInfoResponse googleLoginCallback(String code) throws IOException {
        GoogleLoginUserInfoResponse googleUserInfo = getGoogleUserInfo(
                getGoogleAccessToken(code)
        );
        LoginUserInfoResponse loginUserInfoResponse = new LoginUserInfoResponse();
        loginUserInfoResponse.setEmail(googleUserInfo.getEmail());
        loginUserInfoResponse.setName(googleUserInfo.getName());
        loginUserInfoResponse.setPicture(googleUserInfo.getPicture());
        return loginUserInfoResponse;
    }

    private String getGoogleAccessToken(String code) throws JsonProcessingException {
        String url = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> request = new HttpEntity<>(
                "client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&code=" + code +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + redirectUri,
                headers
        );
        ResponseEntity<String> response = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
        return objectMapper.readTree(response.getBody()).get("access_token").asText();
    }

    private GoogleLoginUserInfoResponse getGoogleUserInfo(String accessToken) throws IOException {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        Headers headers = new Headers.Builder()
                .add("Authorization", "Bearer " + accessToken)
                .build();
        Response response = okHttpClient.newCall(
                new Request.Builder()
                        .url(url)
                        .headers(headers)
                        .build()
        ).execute();
        return objectMapper.readValue(response.body().string(), GoogleLoginUserInfoResponse.class);
    }
}
