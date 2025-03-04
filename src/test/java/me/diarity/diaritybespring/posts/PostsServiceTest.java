package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostsServiceTest {
    @Mock
    private PostsRepository postsRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private PostsService postsService;

    private final Users author = Users.builder()
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
        Instant post1CreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant post1ModifiedAt = Instant.ofEpochMilli(1641081600000L);
        Instant post2CreatedAt = Instant.ofEpochMilli(1642075200000L);
        Instant post2ModifiedAt = Instant.ofEpochMilli(1642161600000L);
        Posts posts1 = Posts.builder()
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
        Posts posts2 = Posts.builder()
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
        when(postsRepository.findAllByOrderByCreatedAtDesc()).thenReturn(
                List.of(posts2, posts1)
        );

        // when
        List<PostsResponse> postsResponses = postsService.getAll();

        // then
        assertThat(postsResponses.get(0).getTitle()).isEqualTo(posts2.getTitle());
        assertThat(postsResponses.get(0).getContent()).isEqualTo(posts2.getContent());
        assertThat(postsResponses.get(0).getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.get(0).getCreatedAt()).isEqualTo(post2CreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.get(0).getModifiedAt()).isEqualTo(post2ModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.get(0).getIsPublic()).isEqualTo(posts2.getIsPublic());
        assertThat(postsResponses.get(0).getIsDeleted()).isEqualTo(posts2.getIsDeleted());
        assertThat(postsResponses.get(0).getDeletedAt()).isEqualTo(posts2.getDeletedAt());
        assertThat(postsResponses.get(0).getLikesCount()).isEqualTo(posts2.getLikesCount());
        assertThat(postsResponses.get(0).getCommentsCount()).isEqualTo(posts2.getCommentsCount());

        assertThat(postsResponses.get(1).getTitle()).isEqualTo(posts1.getTitle());
        assertThat(postsResponses.get(1).getContent()).isEqualTo(posts1.getContent());
        assertThat(postsResponses.get(1).getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.get(1).getCreatedAt()).isEqualTo(post1CreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.get(1).getModifiedAt()).isEqualTo(post1ModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.get(1).getIsPublic()).isEqualTo(posts1.getIsPublic());
        assertThat(postsResponses.get(1).getIsDeleted()).isEqualTo(posts1.getIsDeleted());
        assertThat(postsResponses.get(1).getDeletedAt()).isEqualTo(posts1.getDeletedAt());
        assertThat(postsResponses.get(1).getLikesCount()).isEqualTo(posts1.getLikesCount());
        assertThat(postsResponses.get(1).getCommentsCount()).isEqualTo(posts1.getCommentsCount());
    }

    @Test
    public void create() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        Posts posts = Posts.builder()
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
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        PostsCreateRequest postsCreateRequest = PostsCreateRequest.builder()
                .title("testTitle")
                .content("testContent")
                .authorEmail(author.getEmail())
                .build();

        // when
        PostsResponse postsResponse = postsService.create(postsCreateRequest);

        // then
        assertThat(postsResponse.getTitle()).isEqualTo(posts.getTitle());
        assertThat(postsResponse.getContent()).isEqualTo(posts.getContent());
        assertThat(postsResponse.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponse.getCreatedAt()).isEqualTo(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getModifiedAt()).isEqualTo(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getIsPublic()).isEqualTo(posts.getIsPublic());
        assertThat(postsResponse.getIsDeleted()).isEqualTo(posts.getIsDeleted());
        assertThat(postsResponse.getDeletedAt()).isEqualTo(posts.getDeletedAt());
        assertThat(postsResponse.getLikesCount()).isEqualTo(posts.getLikesCount());
        assertThat(postsResponse.getCommentsCount()).isEqualTo(posts.getCommentsCount());
    }
}
