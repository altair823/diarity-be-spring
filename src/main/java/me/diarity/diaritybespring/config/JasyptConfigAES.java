package me.diarity.diaritybespring.config;


import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.val;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfigAES {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean("jasyptStringEncryptor")
    protected StringEncryptor stringEncryptor() {
        val encryptor = new PooledPBEStringEncryptor();
        val config = new SimpleStringPBEConfig();

        config.setPassword(password); // Encryption key
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // Algorithm
        config.setKeyObtentionIterations("1000"); // Hashing iterations
        config.setPoolSize("1"); // Instance pool size
        config.setProviderName("SunJCE"); // Valid provider name
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt generator class
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64"); // Encoding type
        encryptor.setConfig(config);
        return encryptor;
    }
}