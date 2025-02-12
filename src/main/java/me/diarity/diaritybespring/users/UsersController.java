package me.diarity.diaritybespring.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/users")
public class UsersController {
    @GetMapping()
    public String index() {
        return "Hello, World!";
    }
}
