package me.diarity.diaritybespring.posts.comments;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.PostsRepository;
import me.diarity.diaritybespring.posts.comments.dto.*;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import me.diarity.diaritybespring.posts.comments.entity.CommentsLikes;
import me.diarity.diaritybespring.posts.comments.repository.CommentsHierarchyRepository;
import me.diarity.diaritybespring.posts.comments.repository.CommentsLikesRepository;
import me.diarity.diaritybespring.posts.comments.repository.CommentsRepository;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentRepository;
    private final CommentsHierarchyRepository commentsHierarchyRepository;
    private final CommentsLikesRepository commentsLikesRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public CommentsResponse create(CommentsCreateRequest commentsCreateRequest, String email, Long postId) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        post.addComment();
        post = postsRepository.save(post);
        Comments comments = CommentsMapper.INSTANCE.toEntity(
                commentsCreateRequest,
                user,
                post
        );
        comments = commentRepository.save(comments);
        Long parentCommentId = commentsCreateRequest.getParentCommentId();
        if (parentCommentId != null) {
            Comments parentComments = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
            CommentsHierarchy commentsHierarchy =
                    CommentsHierarchy.builder()
                            .id(CommentsHierarchyId.builder()
                                    .parentCommentId(parentComments.getId())
                                    .childCommentId(comments.getId())
                                    .build())
                            .parentComments(parentComments)
                            .childComments(comments)
                            .build();
            commentsHierarchyRepository.save(commentsHierarchy);
        }

        CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comments);
        commentsResponse.setParentCommentId(parentCommentId);
        return commentsResponse;
    }

    public List<CommentsResponse> findAll(Long postId, String userEmail) {
        postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        if (userEmail.equals("anonymousUser")) {
            List<Comments> comments = commentRepository.findAllByPostId(postId);
            return comments.stream().map(comment -> {
                CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comment);
                commentsHierarchyRepository.findByChildCommentsId(comment.getId())
                        .ifPresent(commentsHierarchy ->
                                commentsResponse.setParentCommentId(
                                        commentsHierarchy.getId().getParentCommentId()
                                )
                        );
                commentsResponse.setIsLiked(false);
                return commentsResponse;
            }).toList();
        } else {
            Users user = usersRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
            List<CommentsWithLikeResponse> comments = commentRepository.findAllByPostIdWithLike(postId, user.getId());
            return comments.stream().map(comment -> {
                CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comment);
                commentsResponse.setIsLiked(comment.getIsLiked());
                return commentsResponse;
            }).toList();
        }
    }

    @Transactional
    public CommentsResponse like(Long commentId, String userEmail) {
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        if (commentsLikesRepository.existsByCommentIdAndUserId(
                comments.getId(),
                user.getId())) {
            throw new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
        }
        comments.addLike();
        comments = commentRepository.save(comments);
        commentsLikesRepository.save(
                CommentsLikes.builder()
                        .id(CommentsLikesId.builder()
                                .commentId(comments.getId())
                                .userId(user.getId())
                                .build())
                        .comment(comments)
                        .user(user)
                        .build()
        );

        return CommentsMapper.INSTANCE.toResponse(comments);
    }

    @Transactional
    public CommentsResponse unlike(Long commentId, String userEmail) {
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        if (!commentsLikesRepository.existsByCommentIdAndUserId(
                comments.getId(),
                user.getId())) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 댓글입니다.");
        }
        comments.removeLike();
        commentRepository.save(comments);
        commentsLikesRepository.delete(
                CommentsLikes.builder()
                        .id(CommentsLikesId.builder()
                                .commentId(comments.getId())
                                .userId(user.getId())
                                .build())
                        .comment(comments)
                        .user(user)
                        .build()
        );

        return CommentsMapper.INSTANCE.toResponse(comments);
    }
}