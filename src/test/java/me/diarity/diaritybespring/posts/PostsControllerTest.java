package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostsControllerTest {
    @Mock
    private PostsService postsService;

    @InjectMocks
    private PostsController postsController;

    private final UsersResponse author = UsersResponse.builder()
            .id(1L)
            .email("testemail@gmail.com")
            .name("testUser")
            .picture("testPicture")
            .role("NORMAL")
            .displayName("testUser")
            .build();

    @BeforeEach
    public void setUp() {
        // Create a mock Authentication object
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(author.getEmail());

        // Create a mock SecurityContext object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getAll() {
        // given

        LocalDateTime post1CreatedAt = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime post1ModifiedAt = LocalDateTime.of(2022, 1, 2, 0, 0);
        LocalDateTime post2CreatedAt = LocalDateTime.of(2022, 1, 3, 0, 0);
        LocalDateTime post2ModifiedAt = LocalDateTime.of(2022, 1, 4, 0, 0);
        PostsResponse postsResponse1 = PostsResponse.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(post1CreatedAt)
                .modifiedAt(post1ModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .commentsCount(0)
                .build();
        PostsResponse postsResponse2 = PostsResponse.builder()
                .id(2L)
                .title("testTitle2")
                .content("testContent2")
                .author(author)
                .createdAt(post2CreatedAt)
                .modifiedAt(post2ModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .commentsCount(0)
                .build();
        when(postsService.getAll(any())).thenReturn(
                List.of(postsResponse2, postsResponse1)
        );

        // when
        List<PostsResponse> postsResponses = postsController.getAll();

        // then
        assertThat(postsResponses.getFirst().getId()).isEqualTo(2L);
        assertThat(postsResponses.getFirst().getTitle()).isEqualTo("testTitle2");
        assertThat(postsResponses.getFirst().getContent()).isEqualTo("testContent2");
        assertThat(postsResponses.getFirst().getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.getFirst().getCreatedAt()).isEqualTo(post2CreatedAt);
        assertThat(postsResponses.getFirst().getModifiedAt()).isEqualTo(post2ModifiedAt);
        assertThat(postsResponses.getFirst().getIsPublic()).isEqualTo(true);
        assertThat(postsResponses.getFirst().getIsDeleted()).isEqualTo(false);
        assertThat(postsResponses.getFirst().getDeletedAt()).isNull();
        assertThat(postsResponses.getFirst().getLikesCount()).isEqualTo(0);
        assertThat(postsResponses.getFirst().getCommentsCount()).isEqualTo(0);

        assertThat(postsResponses.get(1).getId()).isEqualTo(1L);
        assertThat(postsResponses.get(1).getTitle()).isEqualTo("testTitle");
        assertThat(postsResponses.get(1).getContent()).isEqualTo("testContent");
        assertThat(postsResponses.get(1).getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.get(1).getCreatedAt()).isEqualTo(post1CreatedAt);
        assertThat(postsResponses.get(1).getModifiedAt()).isEqualTo(post1ModifiedAt);
        assertThat(postsResponses.get(1).getIsPublic()).isEqualTo(true);
        assertThat(postsResponses.get(1).getIsDeleted()).isEqualTo(false);
        assertThat(postsResponses.get(1).getDeletedAt()).isNull();
        assertThat(postsResponses.get(1).getLikesCount()).isEqualTo(0);
        assertThat(postsResponses.get(1).getCommentsCount()).isEqualTo(0);
    }

    @Test
    public void create() {
        // given
        LocalDateTime postCreatedAt = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime postModifiedAt = LocalDateTime.of(2022, 1, 2, 0, 0);
        PostsResponse postsResponse = PostsResponse.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(postCreatedAt)
                .modifiedAt(postModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .commentsCount(0)
                .build();
        PostsCreateRequest postsCreateRequest = PostsCreateRequest.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        when(postsService.create(postsCreateRequest, author.getEmail())).thenReturn(postsResponse);

        // when
        PostsResponse createdPostsResponse = postsController.create(postsCreateRequest);

        // then
        assertThat(createdPostsResponse.getId()).isEqualTo(1L);
        assertThat(createdPostsResponse.getTitle()).isEqualTo("testTitle");
        assertThat(createdPostsResponse.getContent()).isEqualTo("testContent");
        assertThat(createdPostsResponse.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(createdPostsResponse.getCreatedAt()).isEqualTo(postCreatedAt);
        assertThat(createdPostsResponse.getModifiedAt()).isEqualTo(postModifiedAt);
        assertThat(createdPostsResponse.getIsPublic()).isEqualTo(true);
        assertThat(createdPostsResponse.getIsDeleted()).isEqualTo(false);
        assertThat(createdPostsResponse.getDeletedAt()).isNull();
        assertThat(createdPostsResponse.getLikesCount()).isEqualTo(0);
        assertThat(createdPostsResponse.getCommentsCount()).isEqualTo(0);
    }

    @Test
    public void getPostById() {
        // given
        LocalDateTime postCreatedAt = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime postModifiedAt = LocalDateTime.of(2022, 1, 2, 0, 0);
        PostsResponse postsResponse = PostsResponse.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(postCreatedAt)
                .modifiedAt(postModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .commentsCount(0)
                .build();
        when(postsService.findById(1L)).thenReturn(postsResponse);

        // when
        PostsResponse postById = postsController.getPostById(1L);

        // then
        assertThat(postById.getId()).isEqualTo(1L);
        assertThat(postById.getTitle()).isEqualTo("testTitle");
        assertThat(postById.getContent()).isEqualTo("testContent");
        assertThat(postById.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postById.getCreatedAt()).isEqualTo(postCreatedAt);
        assertThat(postById.getModifiedAt()).isEqualTo(postModifiedAt);
        assertThat(postById.getIsPublic()).isEqualTo(true);
        assertThat(postById.getIsDeleted()).isEqualTo(false);
        assertThat(postById.getDeletedAt()).isNull();
        assertThat(postById.getLikesCount()).isEqualTo(0);
        assertThat(postById.getCommentsCount()).isEqualTo(0);
    }

    @Test
    public void like() {
        // given
        LocalDateTime postCreatedAt = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime postModifiedAt = LocalDateTime.of(2022, 1, 2, 0, 0);
        PostsResponse postsResponse = PostsResponse.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(postCreatedAt)
                .modifiedAt(postModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(1)
                .commentsCount(0)
                .build();
        when(postsService.like(1L, author.getEmail())).thenReturn(postsResponse);

        // when
        PostsResponse likedPostsResponse = postsController.like(1L);

        // then
        assertThat(likedPostsResponse.getId()).isEqualTo(1L);
        assertThat(likedPostsResponse.getTitle()).isEqualTo("testTitle");
        assertThat(likedPostsResponse.getContent()).isEqualTo("testContent");
        assertThat(likedPostsResponse.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(likedPostsResponse.getCreatedAt()).isEqualTo(postCreatedAt);
        assertThat(likedPostsResponse.getModifiedAt()).isEqualTo(postModifiedAt);
        assertThat(likedPostsResponse.getIsPublic()).isEqualTo(true);
        assertThat(likedPostsResponse.getIsDeleted()).isEqualTo(false);
        assertThat(likedPostsResponse.getDeletedAt()).isNull();
        assertThat(likedPostsResponse.getLikesCount()).isEqualTo(1);
        assertThat(likedPostsResponse.getCommentsCount()).isEqualTo(0);
    }

    @Test
    public void likeDuplicate() {
        // given
        when(postsService.like(1L, author.getEmail())).thenThrow(new IllegalArgumentException("이미 좋아요를 누른 게시글입니다."));

        // when
        // then
        // throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> postsController.like(1L));
    }

    @Test
    public void unlike() {
        // given
        LocalDateTime postCreatedAt = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime postModifiedAt = LocalDateTime.of(2022, 1, 2, 0, 0);
        PostsResponse postsResponse = PostsResponse.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(postCreatedAt)
                .modifiedAt(postModifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .commentsCount(0)
                .build();
        when(postsService.unlike(1L, author.getEmail())).thenReturn(postsResponse);

        // when
        PostsResponse unlikedPostsResponse = postsController.unlike(1L);

        // then
        assertThat(unlikedPostsResponse.getId()).isEqualTo(1L);
        assertThat(unlikedPostsResponse.getTitle()).isEqualTo("testTitle");
        assertThat(unlikedPostsResponse.getContent()).isEqualTo("testContent");
        assertThat(unlikedPostsResponse.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(unlikedPostsResponse.getCreatedAt()).isEqualTo(postCreatedAt);
        assertThat(unlikedPostsResponse.getModifiedAt()).isEqualTo(postModifiedAt);
        assertThat(unlikedPostsResponse.getIsPublic()).isEqualTo(true);
        assertThat(unlikedPostsResponse.getIsDeleted()).isEqualTo(false);
        assertThat(unlikedPostsResponse.getDeletedAt()).isNull();
        assertThat(unlikedPostsResponse.getLikesCount()).isEqualTo(0);
        assertThat(unlikedPostsResponse.getCommentsCount()).isEqualTo(0);
    }

    @Test
    public void unlikeFail() {
        // given
        when(postsService.unlike(1L, author.getEmail())).thenThrow(new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다."));

        // when
        // then
        // throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> postsController.unlike(1L));
    }
}
