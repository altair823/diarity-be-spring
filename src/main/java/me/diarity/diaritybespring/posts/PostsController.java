package me.diarity.diaritybespring.posts;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @GetMapping
    public List<PostsResponse> getAll() {
        // 로그인한 사용자와 그렇지 않은 사용자에게 다른 정보를 보여주기 위해 Authentication 객체를 사용
        // 로그인하지 않은 사용자는 "anonymousUser"로 출력됨
        // 로그인한 사용자는 Users 객체의 이메일이 출력됨
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postsService.getAll(authentication.getPrincipal().toString());
    }

    @PostMapping
    public PostsResponse create(@RequestBody PostsCreateRequest postsCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postsService.create(postsCreateRequest, authentication.getPrincipal().toString());
    }

    @GetMapping("/{id}")
    public PostsResponse getPostById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @PostMapping("/{id}/like")
    public PostsResponse like(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postsService.like(id, authentication.getPrincipal().toString());
    }

    @DeleteMapping("/{id}/like")
    public PostsResponse unlike(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postsService.unlike(id, authentication.getPrincipal().toString());
    }
}
