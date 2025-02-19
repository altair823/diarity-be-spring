package me.diarity.diaritybespring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/index")
    public String index() {
        return "Hello, World! version0.0.1 by Diarity";
    }
}
