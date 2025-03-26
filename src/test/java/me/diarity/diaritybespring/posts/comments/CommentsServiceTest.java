package me.diarity.diaritybespring.posts.comments;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.PostsRepository;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsHierarchyId;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.comments.dto.CommentsWithLikeResponse;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import me.diarity.diaritybespring.posts.comments.repository.CommentsHierarchyRepository;
import me.diarity.diaritybespring.posts.comments.repository.CommentsLikesRepository;
import me.diarity.diaritybespring.posts.comments.repository.CommentsRepository;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentsServiceTest {
    @Mock
    private CommentsRepository commentsRepository;
    @Mock
    private CommentsHierarchyRepository commentsHierarchyRepository;
    @Mock
    private CommentsLikesRepository commentsLikesRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private CommentsService commentsService;

    private final Users user = Users.builder()
            .id(1L)
            .email("testemail@gmail.com")
            .name("testUser")
            .picture("testPicture")
            .role("NORMAL")
            .displayName("testUser")
            .build();

    private final Posts post = Posts.builder()
            .id(2L)
            .title("testTitle")
            .content("testContent")
            .author(user)
            .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC))
            .modifiedAt(LocalDateTime.of(2021, 1, 2, 0, 0, 0).toInstant(ZoneOffset.UTC))
            .isPublic(true)
            .isDeleted(false)
            .deletedAt(null)
            .likesCount(0)
            .commentsCount(0)
            .build();

    private final Instant createdAt = Instant.ofEpochMilli(1612137600000L);
    private final Instant modifiedAt = Instant.ofEpochMilli(1612224000000L);
    private final Comments comments = Comments.builder()
            .id(3L)
            .content("testComment")
            .author(user)
            .post(post)
            .createdAt(createdAt)
            .modifiedAt(modifiedAt)
            .isDeleted(false)
            .deletedAt(null)
            .likesCount(0)
            .build();

    private static void assertCommentsResponse(CommentsResponse commentsResponses, Comments comments, Users user, Posts post) {
        assertThat(commentsResponses.getId()).isEqualTo(comments.getId());
        assertThat(commentsResponses.getUserId()).isEqualTo(user.getId());
        assertThat(commentsResponses.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(commentsResponses.getPicture()).isEqualTo(user.getPicture());
        assertThat(commentsResponses.getContent()).isEqualTo(comments.getContent());
        assertThat(commentsResponses.getLikesCount()).isEqualTo(comments.getLikesCount());
        assertThat(commentsResponses.getIsLiked()).isFalse();
        assertThat(commentsResponses.getPostId()).isEqualTo(post.getId());
        assertThat(commentsResponses.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(comments.getCreatedAt(), ZoneOffset.systemDefault()));
        assertThat(commentsResponses.getModifiedAt()).isEqualTo(LocalDateTime.ofInstant(comments.getModifiedAt(), ZoneOffset.systemDefault()));
    }

    @Test
    public void create() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comments);

        // when
        CommentsResponse commentsResponse = commentsService.create(commentsCreateRequest, user.getEmail(), post.getId());

        // then
        assertCommentsResponse(commentsResponse, comments, user, post);
    }

    @Test
    public void createChildComment() {
        // given
        Instant createdAt = Instant.ofEpochMilli(1612310400000L);
        Instant modifiedAt = Instant.ofEpochMilli(1612396800000L);
        Comments parentComments = Comments.builder()
                .id(4L)
                .content("testParentComment")
                .author(user)
                .post(post)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .build();
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .parentCommentId(parentComments.getId())
                .build();
        CommentsHierarchy commentsHierarchy = CommentsHierarchy.builder()
                .parentComments(parentComments)
                .childComments(comments)
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comments);
        when(commentsRepository.findById(4L)).thenReturn(Optional.of(parentComments));
        when(commentsHierarchyRepository.save(any())).thenReturn(commentsHierarchy);

        // when
        CommentsResponse commentsResponse = commentsService.create(commentsCreateRequest, user.getEmail(), post.getId());

        // then
        assertThat(commentsResponse.getId()).isEqualTo(comments.getId());
        assertThat(commentsResponse.getUserId()).isEqualTo(user.getId());
        assertThat(commentsResponse.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(commentsResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(commentsResponse.getContent()).isEqualTo(comments.getContent());
        assertThat(commentsResponse.getLikesCount()).isEqualTo(comments.getLikesCount());
        assertThat(commentsResponse.getIsLiked()).isFalse();
        assertThat(commentsResponse.getPostId()).isEqualTo(post.getId());
        assertThat(commentsResponse.getParentCommentId()).isEqualTo(parentComments.getId());
        assertThat(commentsResponse.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(comments.getCreatedAt(), ZoneOffset.systemDefault()));
        assertThat(commentsResponse.getModifiedAt()).isEqualTo(LocalDateTime.ofInstant(comments.getModifiedAt(), ZoneOffset.systemDefault()));
    }

    @Test
    public void createFailToFindUser() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void createFailToFindPost() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void createFailToFindParentComment() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .parentCommentId(4L)
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.findById(4L)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("해당 댓글이 없습니다.");
    }

    @Test
    public void createFailToSaveComment() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenThrow(new IllegalArgumentException("댓글 저장에 실패했습니다."));

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("댓글 저장에 실패했습니다.");
    }

    @Test
    public void createFailToSaveCommentHierarchy() {
        // given
        Instant createdAt = Instant.ofEpochMilli(1612310400000L);
        Instant modifiedAt = Instant.ofEpochMilli(1612396800000L);
        Comments parentComments = Comments.builder()
                .id(4L)
                .content("testParentComment")
                .author(user)
                .post(post)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .build();
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comments.getContent())
                .parentCommentId(parentComments.getId())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comments);
        when(commentsRepository.findById(4L)).thenReturn(Optional.of(parentComments));
        when(commentsHierarchyRepository.save(any())).thenThrow(new IllegalArgumentException("댓글 계층 저장에 실패했습니다."));

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("댓글 계층 저장에 실패했습니다.");
    }

    @Test
    public void findAll() {
        // given
        Comments comments2 = Comments.builder()
                .id(4L)
                .content("testComment2")
                .author(user)
                .post(post)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .build();
        Instant createdAtChild = Instant.ofEpochMilli(1612489600000L);
        Instant modifiedAtChild = Instant.ofEpochMilli(1612576000000L);
        Comments childComments = Comments.builder()
                .id(5L)
                .content("testChildComment")
                .author(user)
                .post(post)
                .createdAt(createdAtChild)
                .modifiedAt(modifiedAtChild)
                .isDeleted(false)
                .deletedAt(null)
                .likesCount(0)
                .build();
        CommentsHierarchy commentsHierarchy = CommentsHierarchy.builder()
                .id(new CommentsHierarchyId(
                        comments2.getId(),
                        childComments.getId()
                ))
                .parentComments(comments2)
                .childComments(childComments)
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.findAllByPostIdWithLike(post.getId(), user.getId())).thenReturn(List.of(
                CommentsWithLikeResponse.builder()
                        .id(comments.getId())
                        .authorUserId(user.getId())
                        .authorDisplayName(user.getDisplayName())
                        .authorPicture(user.getPicture())
                        .createdAt(createdAt)
                        .modifiedAt(modifiedAt)
                        .content(comments.getContent())
                        .isDeleted(comments.getIsDeleted())
                        .deletedAt(comments.getDeletedAt())
                        .likesCount(comments.getLikesCount())
                        .isLiked(false)
                        .postId(post.getId())
                        .build(),
                CommentsWithLikeResponse.builder()
                        .id(comments2.getId())
                        .authorUserId(user.getId())
                        .authorDisplayName(user.getDisplayName())
                        .authorPicture(user.getPicture())
                        .createdAt(createdAt)
                        .modifiedAt(modifiedAt)
                        .content(comments2.getContent())
                        .isDeleted(comments2.getIsDeleted())
                        .deletedAt(comments2.getDeletedAt())
                        .likesCount(comments2.getLikesCount())
                        .isLiked(false)
                        .postId(post.getId())
                        .build(),
                CommentsWithLikeResponse.builder()
                        .id(childComments.getId())
                        .authorUserId(user.getId())
                        .authorDisplayName(user.getDisplayName())
                        .authorPicture(user.getPicture())
                        .createdAt(createdAtChild)
                        .modifiedAt(modifiedAtChild)
                        .content(childComments.getContent())
                        .isDeleted(childComments.getIsDeleted())
                        .deletedAt(childComments.getDeletedAt())
                        .likesCount(childComments.getLikesCount())
                        .isLiked(false)
                        .postId(post.getId())
                        .parentCommentId(4L)
                        .build()
        ));
        when(commentsHierarchyRepository.findByChildCommentsId(childComments.getId()))
                .thenReturn(Optional.ofNullable(commentsHierarchy));

        // when
        List<CommentsResponse> commentsResponses = commentsService.findAll(post.getId(), user.getEmail());
        for (CommentsResponse commentsResponse : commentsResponses) {
            System.out.println(commentsResponse.getId());
        }

        // then
        assertThat(commentsResponses.size()).isEqualTo(3);
        assertCommentsResponse(commentsResponses.getFirst(), comments, user, post);
        assertCommentsResponse(commentsResponses.get(1), comments2, user, post);
        assertCommentsResponse(commentsResponses.getLast(), childComments, user, post);
        assertThat(commentsResponses.getLast().getParentCommentId()).isEqualTo(comments2.getId());
    }

    @Test
    public void findAllFailToFindPost() {
        // given
        when(postsRepository.findById(post.getId())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.findAll(post.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 게시글이 없습니다.");
    }

    @Test
    public void like() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentsLikesRepository.existsByCommentIdAndUserId(comments.getId(), user.getId())).thenReturn(false);
        when(commentsRepository.save(any())).thenReturn(comments);


        // when
        CommentsResponse commentsResponse = commentsService.like(comments.getId(), user.getEmail());

        // then
        assertCommentsResponse(commentsResponse, comments, user, post);
    }

    @Test
    public void likeFailToFindComment() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.like(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 댓글이 없습니다.");
    }

    @Test
    public void likeFailToFindUser() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.like(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void likeFailToSaveComment() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentsLikesRepository.existsByCommentIdAndUserId(comments.getId(), user.getId())).thenReturn(true);

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.like(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("이미 좋아요를 누른 댓글입니다.");
    }

    @Test
    public void unlike() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentsLikesRepository.existsByCommentIdAndUserId(comments.getId(), user.getId())).thenReturn(true);
        when(commentsRepository.save(any())).thenReturn(comments);

        // when
        CommentsResponse commentsResponse = commentsService.unlike(comments.getId(), user.getEmail());

        // then
        assertCommentsResponse(commentsResponse, comments, user, post);
    }

    @Test
    public void unlikeFailToFindComment() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.unlike(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 댓글이 없습니다.");
    }

    @Test
    public void unlikeFailToFindUser() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.unlike(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void unlikeFailToSaveComment() {
        // given
        when(commentsRepository.findById(comments.getId())).thenReturn(Optional.of(comments));
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentsLikesRepository.existsByCommentIdAndUserId(comments.getId(), user.getId())).thenReturn(false);

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentsService.unlike(comments.getId(), user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("좋아요를 누르지 않은 댓글입니다.");
    }
}
