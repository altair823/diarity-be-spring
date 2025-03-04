package me.diarity.diaritybespring.posts;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @GetMapping
    public List<PostsResponse> getAll() {
        return postsService.getAll();
    }

    @PostMapping
    public PostsResponse create(@RequestBody PostsCreateRequest postsCreateRequest) {
        return postsService.create(postsCreateRequest);
    }
}
