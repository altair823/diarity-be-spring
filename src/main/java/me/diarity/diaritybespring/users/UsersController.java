package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersRepository usersRepository;

    @GetMapping
    public List<Users> getUsers() {
        List<Users> allUsers = usersRepository.findAll();
        System.out.println(allUsers);
        return allUsers;
    }
}
