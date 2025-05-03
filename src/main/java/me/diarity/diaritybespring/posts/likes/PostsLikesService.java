package me.diarity.diaritybespring.posts.likes;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesId;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsLikesService {
    private final PostsLikesRepository postsLikesRepository;

    @Transactional
    public Optional<PostsLikesResponse> like(PostsLikesRequest postsLikesRequest) {
        PostsLikes likes = PostsLikes.builder()
                .id(PostsLikesId.builder()
                        .postId(postsLikesRequest.getPost().getId())
                        .userId(postsLikesRequest.getUser().getId())
                        .build())
                .post(postsLikesRequest.getPost())
                .user(postsLikesRequest.getUser())
                .build();
        if (postsLikesRepository.existsByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId())) {
            return Optional.empty();
        }
        postsLikesRepository.save(likes);
        return Optional.of(PostsLikesResponse.builder()
                .postId(likes.getPost().getId())
                .userId(likes.getUser().getId())
                .build());
    }

    @Transactional
    public Optional<PostsLikesResponse> unlike(PostsLikesRequest postsLikesRequest) {
        PostsLikes likes = PostsLikes.builder()
                .post(postsLikesRequest.getPost())
                .user(postsLikesRequest.getUser())
                .build();
        if (!postsLikesRepository.existsByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId())) {
            return Optional.empty();
        }
        postsLikesRepository.deleteByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId());
        return Optional.of(PostsLikesResponse.builder()
                .postId(likes.getPost().getId())
                .userId(likes.getUser().getId())
                .build());
    }
}
