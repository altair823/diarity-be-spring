package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.posts.likes.LikesService;
import me.diarity.diaritybespring.posts.likes.dto.LikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.LikesResponse;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
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
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostsServiceTest {
    @Mock
    private PostsRepository postsRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private LikesService likesService;

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
                .build();

        // when
        PostsResponse postsResponse = postsService.create(postsCreateRequest, author.getEmail());

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

    @Test
    public void createFail() {
        // given
        PostsCreateRequest postsCreateRequest = PostsCreateRequest.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.create(postsCreateRequest, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void getPostById() {
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
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));

        // when
        PostsResponse postsResponse = postsService.getPostById(1L);

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

    @Test
    public void getPostByIdFail() {
        // given
        when(postsRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.getPostById(1L));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void like() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        int initialLikesCount = 0;
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
                .likesCount(initialLikesCount)
                .commentsCount(0)
                .build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(likesService.like(LikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.of(LikesResponse.builder()
                .postId(posts.getId())
                .userId(author.getId())
                .build()));

        // when
        PostsResponse postsResponse = postsService.like(1L, author.getEmail());

        // then
        assertThat(postsResponse.getTitle()).isEqualTo(posts.getTitle());
        assertThat(postsResponse.getContent()).isEqualTo(posts.getContent());
        assertThat(postsResponse.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponse.getCreatedAt()).isEqualTo(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getModifiedAt()).isEqualTo(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getIsPublic()).isEqualTo(posts.getIsPublic());
        assertThat(postsResponse.getIsDeleted()).isEqualTo(posts.getIsDeleted());
        assertThat(postsResponse.getDeletedAt()).isEqualTo(posts.getDeletedAt());
        assertThat(postsResponse.getLikesCount()).isEqualTo(initialLikesCount + 1);
        assertThat(postsResponse.getCommentsCount()).isEqualTo(posts.getCommentsCount());
    }

    @Test
    public void likeFail() {
        // given
        when(postsRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.like(1L, any()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void likeDuplicate() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        int initialLikesCount = 0;
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
                .likesCount(initialLikesCount)
                .commentsCount(0)
                .build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(likesService.like(LikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.like(1L, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("이미 좋아요를 누른 게시글입니다.");
    }
}
