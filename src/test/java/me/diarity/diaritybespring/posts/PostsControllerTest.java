package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        when(postsService.getAll()).thenReturn(
                List.of(postsResponse2, postsResponse1)
        );

        // when
        List<PostsResponse> postsResponses = postsController.getAll();

        // then
        assertThat(postsResponses.get(0).getId()).isEqualTo(2L);
        assertThat(postsResponses.get(0).getTitle()).isEqualTo("testTitle2");
        assertThat(postsResponses.get(0).getContent()).isEqualTo("testContent2");
        assertThat(postsResponses.get(0).getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.get(0).getCreatedAt()).isEqualTo(post2CreatedAt);
        assertThat(postsResponses.get(0).getModifiedAt()).isEqualTo(post2ModifiedAt);
        assertThat(postsResponses.get(0).getIsPublic()).isEqualTo(true);
        assertThat(postsResponses.get(0).getIsDeleted()).isEqualTo(false);
        assertThat(postsResponses.get(0).getDeletedAt()).isNull();
        assertThat(postsResponses.get(0).getLikesCount()).isEqualTo(0);
        assertThat(postsResponses.get(0).getCommentsCount()).isEqualTo(0);

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
                .authorEmail(author.getEmail())
                .build();
        when(postsService.create(postsCreateRequest)).thenReturn(postsResponse);

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
}
