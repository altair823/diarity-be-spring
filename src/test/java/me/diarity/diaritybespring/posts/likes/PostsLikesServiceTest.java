package me.diarity.diaritybespring.posts.likes;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesResponse;
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
public class PostsLikesServiceTest {
    @Mock
    private PostsLikesRepository postsLikesRepository;

    @InjectMocks
    private PostsLikesService postsLikesService;

    @Test
    public void like() {
        // given
        PostsLikesRequest postsLikesRequest = PostsLikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(postsLikesRepository.existsByPostIdAndUserId(postsLikesRequest.getPost().getId(), postsLikesRequest.getUser().getId())).thenReturn(false);
        when(postsLikesRepository.save(any())).thenReturn(null);

        // when
        Optional<PostsLikesResponse> postsLikesResponse = postsLikesService.like(postsLikesRequest);

        // then
        assertThat(postsLikesResponse.isPresent()).isTrue();
        assertThat(postsLikesResponse.get().getPostId()).isEqualTo(postsLikesRequest.getPost().getId());
        assertThat(postsLikesResponse.get().getUserId()).isEqualTo(postsLikesRequest.getUser().getId());
    }

    @Test
    public void likeFail() {
        // given
        PostsLikesRequest postsLikesRequest = PostsLikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(postsLikesRepository.existsByPostIdAndUserId(postsLikesRequest.getPost().getId(), postsLikesRequest.getUser().getId())).thenReturn(true);

        // when
        Optional<PostsLikesResponse> postsLikesResponse = postsLikesService.like(postsLikesRequest);

        // then
        assertThat(postsLikesResponse.isPresent()).isFalse();
    }

    @Test
    public void unlike() {
        // given
        PostsLikesRequest postsLikesRequest = PostsLikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(postsLikesRepository.existsByPostIdAndUserId(postsLikesRequest.getPost().getId(), postsLikesRequest.getUser().getId())).thenReturn(true);
        when(postsLikesRepository.save(any())).thenReturn(null);

        // when
        Optional<PostsLikesResponse> postsLikesResponse = postsLikesService.unlike(postsLikesRequest);

        // then
        assertThat(postsLikesResponse.isPresent()).isTrue();
        assertThat(postsLikesResponse.get().getPostId()).isEqualTo(postsLikesRequest.getPost().getId());
        assertThat(postsLikesResponse.get().getUserId()).isEqualTo(postsLikesRequest.getUser().getId());
    }

    @Test
    public void unlikeFail() {
        // given
        PostsLikesRequest postsLikesRequest = PostsLikesRequest.builder()
                .post(Posts.builder().id(1L).build())
                .user(Users.builder().id(1L).build())
                .build();
        when(postsLikesRepository.existsByPostIdAndUserId(postsLikesRequest.getPost().getId(), postsLikesRequest.getUser().getId())).thenReturn(false);

        // when
        Optional<PostsLikesResponse> postsLikesResponse = postsLikesService.unlike(postsLikesRequest);

        // then
        assertThat(postsLikesResponse.isPresent()).isFalse();
    }
}
