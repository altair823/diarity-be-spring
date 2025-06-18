package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import me.diarity.diaritybespring.users.UsersRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static me.diarity.diaritybespring.posts.PostsTestUtils.assertPostsResponse;
import static me.diarity.diaritybespring.posts.PostsTestUtils.createPosts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostsServiceOnlyReadUserTest {
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
            .role(UsersRole.READ_ONLY)
            .displayName("testUser")
            .build();

    private final Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
    private final Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
    private final Instant post2CreatedAt = Instant.ofEpochMilli(1642075200000L);
    private final Instant post2ModifiedAt = Instant.ofEpochMilli(1642161600000L);

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
    public void findAll() {
        // given
        Posts posts1 = createPosts(author, postCreatedAt, postModifiedAt, 0);
        Posts posts2 = createPosts(author, post2CreatedAt, post2ModifiedAt, 0);
        when(postsRepository.findAllByOrderByCreatedAtDescWithLiked(1L)).thenReturn(
                List.of(
                        List.of(posts2, false).toArray(),
                        List.of(posts1, true).toArray()
                )
        );
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

        // when
        List<PostsResponse> postsResponses = postsService.findAll(author.getEmail());

        // then
        assertPostsResponse(postsResponses.getFirst(), posts2);
        assertPostsResponse(postsResponses.get(1), posts1);
    }

    @Test
    public void create() {
        // given
        Posts posts = createPosts(author, postCreatedAt, postModifiedAt, 0);
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        PostsCreateRequest postsCreateRequest = PostsCreateRequest.builder()
                .title("testTitle")
                .content("testContent")
                .bookTitle("testBookTitle")
                .build();

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.create(postsCreateRequest, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("읽기 전용 사용자는 게시글을 작성할 수 없습니다.");
    }
}
