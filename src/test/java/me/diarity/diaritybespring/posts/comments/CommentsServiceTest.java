package me.diarity.diaritybespring.posts.comments;

import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.PostsRepository;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import me.diarity.diaritybespring.posts.comments.repository.CommentsHierarchyRepository;
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
    private final Comments comment = Comments.builder()
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

    @Test
    public void create() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comment.getContent())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comment);

        // when
        CommentsResponse commentsResponse = commentsService.create(commentsCreateRequest, user.getEmail(), post.getId());

        // then
        assertThat(commentsResponse.getId()).isEqualTo(comment.getId());
        assertThat(commentsResponse.getUserId()).isEqualTo(user.getId());
        assertThat(commentsResponse.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(commentsResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(commentsResponse.getContent()).isEqualTo(comment.getContent());
        assertThat(commentsResponse.getLikesCount()).isEqualTo(comment.getLikesCount());
        assertThat(commentsResponse.getIsLiked()).isFalse();
        assertThat(commentsResponse.getPostId()).isEqualTo(post.getId());
        assertThat(commentsResponse.getParentCommentId()).isNull();
        assertThat(commentsResponse.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(comment.getCreatedAt(), ZoneOffset.systemDefault()));
        assertThat(commentsResponse.getModifiedAt()).isEqualTo(LocalDateTime.ofInstant(comment.getModifiedAt(), ZoneOffset.systemDefault()));
    }

    @Test
    public void createChildComment() {
        // given
        Instant createdAt = Instant.ofEpochMilli(1612310400000L);
        Instant modifiedAt = Instant.ofEpochMilli(1612396800000L);
        Comments parentComment = Comments.builder()
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
                .content(comment.getContent())
                .parentCommentId(parentComment.getId())
                .build();
        CommentsHierarchy commentsHierarchy = CommentsHierarchy.builder()
                .parentComment(parentComment)
                .childComment(comment)
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comment);
        when(commentsRepository.findById(4L)).thenReturn(Optional.of(parentComment));
        when(commentsHierarchyRepository.save(any())).thenReturn(commentsHierarchy);

        // when
        CommentsResponse commentsResponse = commentsService.create(commentsCreateRequest, user.getEmail(), post.getId());

        // then
        assertThat(commentsResponse.getId()).isEqualTo(comment.getId());
        assertThat(commentsResponse.getUserId()).isEqualTo(user.getId());
        assertThat(commentsResponse.getDisplayName()).isEqualTo(user.getDisplayName());
        assertThat(commentsResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(commentsResponse.getContent()).isEqualTo(comment.getContent());
        assertThat(commentsResponse.getLikesCount()).isEqualTo(comment.getLikesCount());
        assertThat(commentsResponse.getIsLiked()).isFalse();
        assertThat(commentsResponse.getPostId()).isEqualTo(post.getId());
        assertThat(commentsResponse.getParentCommentId()).isEqualTo(parentComment.getId());
        assertThat(commentsResponse.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(comment.getCreatedAt(), ZoneOffset.systemDefault()));
        assertThat(commentsResponse.getModifiedAt()).isEqualTo(LocalDateTime.ofInstant(comment.getModifiedAt(), ZoneOffset.systemDefault()));
    }

    @Test
    public void createFailToFindUser() {
        // given
        CommentsCreateRequest commentsCreateRequest = CommentsCreateRequest.builder()
                .content(comment.getContent())
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
                .content(comment.getContent())
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
                .content(comment.getContent())
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
                .content(comment.getContent())
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
        Comments parentComment = Comments.builder()
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
                .content(comment.getContent())
                .parentCommentId(parentComment.getId())
                .build();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(postsRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentsRepository.save(any())).thenReturn(comment);
        when(commentsRepository.findById(4L)).thenReturn(Optional.of(parentComment));
        when(commentsHierarchyRepository.save(any())).thenThrow(new IllegalArgumentException("댓글 계층 저장에 실패했습니다."));

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> commentsService.create(commentsCreateRequest, user.getEmail(), post.getId()));
        assertThat(e.getMessage()).isEqualTo("댓글 계층 저장에 실패했습니다.");
    }
}
