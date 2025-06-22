package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.dto.UsersProfileResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("{id}")
    public UsersProfileResponse getUserProfile(@PathVariable Long id) {
        return usersService.getProfile(id);
    }

    @GetMapping("{id}/posts")
    public List<PostsResponse> getUserPosts(@PathVariable Long id, @RequestParam (required = false) Integer count) {
        return usersService.findPostsByUserId(id, count);
    }
}
