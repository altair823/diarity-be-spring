package me.diarity.diaritybespring.config.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class CustomDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private final Map<String, Slave> slaves = new HashMap<>();
    private final Hikari hikari = new Hikari();

    @Getter
    @Setter
    public static class Slave {
        private String url;
        private String tag;
    }

    @Getter
    @Setter
    public static class Hikari {
        private int maximumPoolSize = 10;
    }
}
