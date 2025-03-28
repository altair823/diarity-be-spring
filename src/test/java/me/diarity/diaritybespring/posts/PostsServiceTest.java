package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.comments.CommentsService;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.dto.PostsCreateRequest;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.posts.dto.PostsWithLikeResponse;
import me.diarity.diaritybespring.posts.likes.PostsLikesService;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesRequest;
import me.diarity.diaritybespring.posts.likes.dto.PostsLikesResponse;
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
    private PostsLikesService postsLikesService;
    @Mock
    private CommentsService commentsService;

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


    private final Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
    private final Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
    private final Instant post2CreatedAt = Instant.ofEpochMilli(1642075200000L);
    private final Instant post2ModifiedAt = Instant.ofEpochMilli(1642161600000L);
    private Posts createPosts(Instant createdAt, Instant modifiedAt, int likesCount) {
        return Posts.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .author(author)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .isPublic(true)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(likesCount)
                .commentsCount(0)
                .build();
    }

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

    private void assertPostsResponse(PostsResponse postsResponses, Posts posts2, Instant post2CreatedAt, Instant post2ModifiedAt) {
        assertThat(postsResponses.getTitle()).isEqualTo(posts2.getTitle());
        assertThat(postsResponses.getContent()).isEqualTo(posts2.getContent());
        assertThat(postsResponses.getAuthor().getEmail()).isEqualTo(author.getEmail());
        assertThat(postsResponses.getCreatedAt()).isEqualTo(post2CreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.getModifiedAt()).isEqualTo(post2ModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponses.getIsPublic()).isEqualTo(posts2.getIsPublic());
        assertThat(postsResponses.getIsDeleted()).isEqualTo(posts2.getIsDeleted());
        assertThat(postsResponses.getDeletedAt()).isEqualTo(posts2.getDeletedAt());
        assertThat(postsResponses.getLikesCount()).isEqualTo(posts2.getLikesCount());
        assertThat(postsResponses.getCommentsCount()).isEqualTo(posts2.getCommentsCount());
    }

    @Test
    public void findAllAnonymousUser() {
        // given
        Posts posts1 = createPosts(postCreatedAt, postModifiedAt, 0);
        Posts posts2 = createPosts(post2CreatedAt, post2ModifiedAt, 0);
        when(postsRepository.findAllByOrderByCreatedAtDesc()).thenReturn(
                List.of(posts2, posts1)
        );

        // when
        List<PostsResponse> postsResponses = postsService.findAll("anonymousUser");

        // then
        assertPostsResponse(postsResponses.getFirst(), posts2, post2CreatedAt, post2ModifiedAt);
        assertPostsResponse(postsResponses.get(1), posts1, postCreatedAt, postModifiedAt);
    }


    @Test
    public void findAllNormalUser() {
        // given
        Posts posts1 = createPosts(postCreatedAt, postModifiedAt, 0);
        Posts posts2 = createPosts(post2CreatedAt, post2ModifiedAt, 0);
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
        assertPostsResponse(postsResponses.getFirst(), posts2, post2CreatedAt, post2ModifiedAt);
        assertPostsResponse(postsResponses.get(1), posts1, postCreatedAt, postModifiedAt);
    }

    @Test
    public void create() {
        // given
        Posts posts = createPosts(postCreatedAt, postModifiedAt, 0);
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        PostsCreateRequest postsCreateRequest = PostsCreateRequest.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        // when
        PostsResponse postsResponse = postsService.create(postsCreateRequest, author.getEmail());

        // then
        assertPostsResponse(postsResponse, posts, postCreatedAt, postModifiedAt);
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
    public void findById() {
        // given
        Posts posts = createPosts(postCreatedAt, postModifiedAt, 0);
        when(postsRepository.findByIdWithLiked(1L, author.getId()))
                .thenReturn(Optional.of(PostsWithLikeResponse.builder()
                        .id(posts.getId())
                        .title(posts.getTitle())
                        .content(posts.getContent())
                        .authorEmail(author.getEmail())
                        .createdAt(postCreatedAt)
                        .modifiedAt(postModifiedAt)
                        .isPublic(posts.getIsPublic())
                        .isDeleted(posts.getIsDeleted())
                        .deletedAt(posts.getDeletedAt())
                        .likesCount(posts.getLikesCount())
                        .commentsCount(posts.getCommentsCount())
                        .isLiked(true)
                        .build()));
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

        // when
        PostsResponse postsResponse = postsService.findById(1L, author.getEmail());

        // then
        assertPostsResponse(postsResponse, posts, postCreatedAt, postModifiedAt);
    }

    @Test
    public void findByIdFail() {
        // given
        when(postsRepository.findByIdWithLiked(1L, author.getId())).thenReturn(Optional.empty());
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> postsService.findById(1L, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void findByIdAnonymousUser() {
        // given
        Posts posts = createPosts(postCreatedAt, postModifiedAt, 0);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));

        // when
        PostsResponse postsResponse = postsService.findById(1L, "anonymousUser");

        // then
        assertPostsResponse(postsResponse, posts, postCreatedAt, postModifiedAt);
    }

    @Test
    public void findByIdAnonymousUserFail() {
        // given
        when(postsRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> postsService.findById(1L, "anonymousUser"));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void like() {
        // given
        int initialLikesCount = 0;
        Posts posts = createPosts(postCreatedAt, postModifiedAt, initialLikesCount);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(postsLikesService.like(PostsLikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.of(PostsLikesResponse.builder()
                .postId(posts.getId())
                .userId(author.getId())
                .build()));

        // when
        PostsResponse postsResponse = postsService.like(1L, author.getEmail());

        // then
        assertPostsResponse(postsResponse, posts, postCreatedAt, postModifiedAt);
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
        int initialLikesCount = 0;
        Posts posts = createPosts(postCreatedAt, postModifiedAt, initialLikesCount);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(postsLikesService.like(PostsLikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.like(1L, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("이미 좋아요를 누른 게시글입니다.");
    }

    @Test
    public void unlike() {
        // given
        int initialLikesCount = 1;
        Posts posts = createPosts(postCreatedAt, postModifiedAt, initialLikesCount);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(postsRepository.save(any())).thenReturn(posts);
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(postsLikesService.unlike(PostsLikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.of(PostsLikesResponse.builder()
                .postId(posts.getId())
                .userId(author.getId())
                .build()));

        // when
        PostsResponse postsResponse = postsService.unlike(1L, author.getEmail());

        // then
        assertPostsResponse(postsResponse, posts, postCreatedAt, postModifiedAt);
    }

    @Test
    public void unlikeFail() {
        // given
        when(postsRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.unlike(1L, any()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void unlikeNotLikedFail() {
        // given
        int initialLikesCount = 0;
        Posts posts = createPosts(postCreatedAt, postModifiedAt, initialLikesCount);
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(usersRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(postsLikesService.unlike(PostsLikesRequest.builder()
                        .post(posts)
                        .user(author)
                        .build())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> postsService.unlike(1L, author.getEmail()));
        assertThat(e.getMessage()).isEqualTo("좋아요를 누르지 않은 게시글입니다.");
    }

    private static void assertCommentsResponse(CommentsResponse commentsResponseResult, CommentsResponse commentsResponse) {
        assertThat(commentsResponseResult.getId()).isEqualTo(commentsResponse.getId());
        assertThat(commentsResponseResult.getUserId()).isEqualTo(commentsResponse.getUserId());
        assertThat(commentsResponseResult.getDisplayName()).isEqualTo(commentsResponse.getDisplayName());
        assertThat(commentsResponseResult.getPicture()).isEqualTo(commentsResponse.getPicture());
        assertThat(commentsResponseResult.getContent()).isEqualTo(commentsResponse.getContent());
        assertThat(commentsResponseResult.getLikesCount()).isEqualTo(commentsResponse.getLikesCount());
        assertThat(commentsResponseResult.getIsLiked()).isEqualTo(commentsResponse.getIsLiked());
        assertThat(commentsResponseResult.getPostId()).isEqualTo(commentsResponse.getPostId());
        assertThat(commentsResponseResult.getParentCommentId()).isEqualTo(commentsResponse.getParentCommentId());
        assertThat(commentsResponseResult.getCreatedAt()).isEqualTo(commentsResponse.getCreatedAt());
        assertThat(commentsResponseResult.getModifiedAt()).isEqualTo(commentsResponse.getModifiedAt());
    }

    @Test
    public void createComment() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        CommentsResponse commentsResponse = CommentsResponse.builder()
                .id(2L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent")
                .likesCount(0)
                .isLiked(false)
                .postId(1L)
                .parentCommentId(null)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content("testContent")
                .build();
        when(commentsService.create(commentsCreateRequest, author.getEmail(), 1L)).thenReturn(commentsResponse);

        // when
        CommentsResponse commentsResponseResult = postsService.createComment(commentsCreateRequest, author.getEmail(), 1L);

        // then
        assertCommentsResponse(commentsResponseResult, commentsResponse);
    }

    @Test
    public void createCommentWithParentComment() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        CommentsResponse commentsResponse = CommentsResponse.builder()
                .id(2L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent")
                .likesCount(0)
                .isLiked(false)
                .postId(1L)
                .parentCommentId(1L)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content("testContent")
                .parentCommentId(1L)
                .build();
        when(commentsService.create(commentsCreateRequest, author.getEmail(), 1L)).thenReturn(commentsResponse);

        // when
        CommentsResponse commentsResponseResult = postsService.createComment(commentsCreateRequest, author.getEmail(), 1L);

        // then
        assertCommentsResponse(commentsResponseResult, commentsResponse);
    }

    @Test
    public void findAllComments() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        CommentsResponse commentsResponse1 = CommentsResponse.builder()
                .id(1L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent")
                .likesCount(0)
                .isLiked(false)
                .postId(1L)
                .parentCommentId(null)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        CommentsResponse commentsResponse2 = CommentsResponse.builder()
                .id(2L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent2")
                .likesCount(0)
                .isLiked(false)
                .postId(1L)
                .parentCommentId(null)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        when(commentsService.findAll(1L, author.getEmail())).thenReturn(List.of(commentsResponse1, commentsResponse2));

        // when
        List<CommentsResponse> commentsResponses = postsService.findAllComments(1L, author.getEmail());

        // then
        assertCommentsResponse(commentsResponses.getFirst(), commentsResponse1);

        assertCommentsResponse(commentsResponses.get(1), commentsResponse2);
    }

    @Test
    public void likeComment() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        CommentsResponse commentsResponse = CommentsResponse.builder()
                .id(1L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent")
                .likesCount(1)
                .isLiked(true)
                .postId(1L)
                .parentCommentId(null)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        when(commentsService.like(1L, author.getEmail())).thenReturn(commentsResponse);

        // when
        CommentsResponse commentsResponseResult = postsService.likeComment(1L, author.getEmail());

        // then
        assertCommentsResponse(commentsResponseResult, commentsResponse);
    }

    @Test
    public void unlikeComment() {
        // given
        Instant postCreatedAt = Instant.ofEpochMilli(1640995200000L);
        Instant postModifiedAt = Instant.ofEpochMilli(1641081600000L);
        CommentsResponse commentsResponse = CommentsResponse.builder()
                .id(1L)
                .userId(author.getId())
                .displayName(author.getDisplayName())
                .picture(author.getPicture())
                .content("testContent")
                .likesCount(0)
                .isLiked(false)
                .postId(1L)
                .parentCommentId(null)
                .createdAt(postCreatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .modifiedAt(postModifiedAt.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        when(commentsService.unlike(1L, author.getEmail())).thenReturn(commentsResponse);

        // when
        CommentsResponse commentsResponseResult = postsService.unlikeComment(1L, author.getEmail());

        // then
        assertCommentsResponse(commentsResponseResult, commentsResponse);
    }
}
