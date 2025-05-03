package me.diarity.diaritybespring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

//@SpringBootTest
public class JasyptTest {
//    @Value("${jasypt.encryptor.password}")
    private String password;

//    @Test
    public void stringEncryptor() {
        String toEncryptString = "";
        System.out.println(jasyptEncoding(toEncryptString));
    }

    private String jasyptEncoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeEnc.setPassword(password);
        pbeEnc.setIvGenerator(new RandomIvGenerator());
        return pbeEnc.encrypt(value);
    }

//    @Test
    public void stringDecrypt() {
        String toDecryptString = "";
        if (!toDecryptString.isBlank()) {
            System.out.println(jasyptDecoding(toDecryptString));
        }
    }

    private String jasyptDecoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeEnc.setPassword(password);
        pbeEnc.setIvGenerator(new RandomIvGenerator());
        return pbeEnc.decrypt(value);
    }
}