package me.diarity.diaritybespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DiarityBeSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiarityBeSpringApplication.class, args);
    }

}
