package me.diarity.diaritybespring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfig {
    @Bean("objectMapper")
    public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .build();
    }
}
