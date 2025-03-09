package me.diarity.diaritybespring.posts;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsMapper;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.posts.likes.LikesService;
import me.diarity.diaritybespring.posts.likes.dto.LikesRequest;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final LikesService likesService;

    public List<PostsResponse> getAll(String userEmail) {
        if (userEmail.equals("anonymousUser")) {
            List<Posts> posts = postsRepository.findAllByOrderByCreatedAtDesc();
            PostsMapper postsMapper = PostsMapper.INSTANCE;
            return posts.stream().map(postsMapper::toResponse).toList();
        } else {
            Users user = usersRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
            List<Object[]> posts = postsRepository.findAllByOrderByCreatedAtDescWithLiked(user.getId());
            PostsMapper postsMapper = PostsMapper.INSTANCE;
            return posts.stream().map(
                    post -> {
                        PostsResponse postsResponse = postsMapper.toResponse((Posts) post[0]);
                        postsResponse.setIsLiked((Boolean) post[1]);
                        return postsResponse;
                    }

            ).toList();
        }
    }

    public PostsResponse create(PostsCreateRequest postsCreateRequest, String userEmail) {
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Posts posts = postsRepository.save(PostsMapper.INSTANCE.toEntity(postsCreateRequest, user));
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse like(Long id, String userEmail) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        likesService.like(
                LikesRequest.builder()
                        .post(posts)
                        .user(user)
                        .build()
        ).orElseThrow(() -> new IllegalArgumentException("이미 좋아요를 누른 게시글입니다."));
        posts.addLike();
        posts = postsRepository.save(posts);
        return PostsMapper.INSTANCE.toResponse(posts);
    }

    public PostsResponse unlike(Long id, String string) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Users user = usersRepository.findByEmail(string)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        likesService.unlike(
                LikesRequest.builder()
                        .post(posts)
                        .user(user)
                        .build()
        ).orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다."));
        posts.removeLike();
        posts = postsRepository.save(posts);
        return PostsMapper.INSTANCE.toResponse(posts);
    }
}
