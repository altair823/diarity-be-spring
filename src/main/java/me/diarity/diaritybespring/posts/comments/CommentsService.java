package me.diarity.diaritybespring.posts.comments;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.PostsRepository;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsHierarchyId;
import me.diarity.diaritybespring.posts.comments.dto.CommentsMapper;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import me.diarity.diaritybespring.posts.comments.repository.CommentsHierarchyRepository;
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
        Comments comment = CommentsMapper.INSTANCE.toEntity(
                commentsCreateRequest,
                user,
                post
        );
        comment = commentRepository.save(comment);
        Long parentCommentId = commentsCreateRequest.getParentCommentId();
        if (parentCommentId != null) {
            Comments parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
            CommentsHierarchy commentsHierarchy =
                    CommentsHierarchy.builder()
                            .id(CommentsHierarchyId.builder()
                                    .parentCommentId(parentComment.getId())
                                    .childCommentId(comment.getId())
                                    .build())
                            .parentComment(parentComment)
                            .childComment(comment)
                            .build();
            commentsHierarchyRepository.save(commentsHierarchy);
        }

        CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comment);
        commentsResponse.setParentCommentId(parentCommentId);
        return commentsResponse;
    }

    public List<CommentsResponse> findAll(Long postId) {
        postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        List<Comments> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(comment -> {
            CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comment);
            commentsHierarchyRepository.findByChildCommentId(comment.getId())
                    .ifPresent(commentsHierarchy ->
                        commentsResponse.setParentCommentId(
                                commentsHierarchy.getId().getParentCommentId()
                        )
                    );
            // TODO: 댓글 좋아요 테이블 만들어서 로직 만들 것!
            commentsResponse.setIsLiked(false);
            return commentsResponse;
        }).toList();
    }
}
