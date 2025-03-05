package me.diarity.diaritybespring.posts.likes;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.likes.dto.LikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.LikesResponse;
import me.diarity.diaritybespring.users.Users;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LikesServiceTest {
    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private LikesService likesService;

    @Test
    public void like() {
        // given
        LikesRequest likesRequest = LikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(likesRepository.existsByPostIdAndUserId(likesRequest.getPost().getId(), likesRequest.getUser().getId())).thenReturn(false);
        when(likesRepository.save(any())).thenReturn(null);

        // when
        Optional<LikesResponse> likesResponse = likesService.like(likesRequest);

        // then
        assertThat(likesResponse.isPresent()).isTrue();
        assertThat(likesResponse.get().getPostId()).isEqualTo(likesRequest.getPost().getId());
        assertThat(likesResponse.get().getUserId()).isEqualTo(likesRequest.getUser().getId());
    }

    @Test
    public void likeFail() {
        // given
        LikesRequest likesRequest = LikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(likesRepository.existsByPostIdAndUserId(likesRequest.getPost().getId(), likesRequest.getUser().getId())).thenReturn(true);

        // when
        Optional<LikesResponse> likesResponse = likesService.like(likesRequest);

        // then
        assertThat(likesResponse.isPresent()).isFalse();
    }

    @Test
    public void unlike() {
        // given
        LikesRequest likesRequest = LikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(likesRepository.existsByPostIdAndUserId(likesRequest.getPost().getId(), likesRequest.getUser().getId())).thenReturn(true);
        when(likesRepository.save(any())).thenReturn(null);

        // when
        Optional<LikesResponse> likesResponse = likesService.unlike(likesRequest);

        // then
        assertThat(likesResponse.isPresent()).isTrue();
        assertThat(likesResponse.get().getPostId()).isEqualTo(likesRequest.getPost().getId());
        assertThat(likesResponse.get().getUserId()).isEqualTo(likesRequest.getUser().getId());
    }

    @Test
    public void unlikeFail() {
        // given
        LikesRequest likesRequest = LikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(likesRepository.existsByPostIdAndUserId(likesRequest.getPost().getId(), likesRequest.getUser().getId())).thenReturn(false);

        // when
        Optional<LikesResponse> likesResponse = likesService.unlike(likesRequest);

        // then
        assertThat(likesResponse.isPresent()).isFalse();
    }
}
