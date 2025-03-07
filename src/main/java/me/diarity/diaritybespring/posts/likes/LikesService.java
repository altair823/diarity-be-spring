package me.diarity.diaritybespring.posts.likes;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.likes.dto.LikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.LikesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    public Optional<LikesResponse> like(LikesRequest likesRequest) {
        Likes likes = Likes.builder()
                .id(LikesId.builder()
                        .postId(likesRequest.getPost().getId())
                        .userId(likesRequest.getUser().getId())
                        .build())
                .post(likesRequest.getPost())
                .user(likesRequest.getUser())
                .build();
        if (likesRepository.existsByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId())) {
            return Optional.empty();
        }
        likesRepository.save(likes);
        return Optional.of(LikesResponse.builder()
                .postId(likes.getPost().getId())
                .userId(likes.getUser().getId())
                .build());
    }

    @Transactional
    public Optional<LikesResponse> unlike(LikesRequest likesRequest) {
        Likes likes = Likes.builder()
                .post(likesRequest.getPost())
                .user(likesRequest.getUser())
                .build();
        if (!likesRepository.existsByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId())) {
            return Optional.empty();
        }
        likesRepository.deleteByPostIdAndUserId(likes.getPost().getId(), likes.getUser().getId());
        return Optional.of(LikesResponse.builder()
                .postId(likes.getPost().getId())
                .userId(likes.getUser().getId())
                .build());
    }
}
