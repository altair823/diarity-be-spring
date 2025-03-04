package me.diarity.diaritybespring.posts;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsMapper;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    public List<PostsResponse> getAll() {
        List<Posts> posts = postsRepository.findAllByOrderByCreatedAtDesc();

        PostsMapper postsMapper = PostsMapper.INSTANCE;
        return posts.stream().map(postsMapper::toResponse).toList();
    }

    public PostsResponse create(PostsCreateRequest postsCreateRequest) {
        Users user = usersRepository.findByEmail(postsCreateRequest.getAuthorEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Posts posts = postsRepository.save(PostsMapper.INSTANCE.toEntity(postsCreateRequest, user));
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse getPostById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse like(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        posts.like();
        postsRepository.save(posts);
        return PostsMapper.INSTANCE.toResponse(posts);
    }
}
