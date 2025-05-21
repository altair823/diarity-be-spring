package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.users.dto.UsersProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.diarity.diaritybespring.utils.UsersContextHandler.getUserEmail;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("profile")
    public UsersProfileResponse getUserProfile() {
        String userEmail = getUserEmail();
        return usersService.getProfile(userEmail);
    }
}
