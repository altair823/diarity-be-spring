package me.diarity.diaritybespring.posts.comments;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.posts.PostsRepository;
import me.diarity.diaritybespring.posts.comments.dto.CommentsCreateRequest;
import me.diarity.diaritybespring.posts.comments.dto.CommentsMapper;
import me.diarity.diaritybespring.posts.comments.dto.CommentsResponse;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import me.diarity.diaritybespring.posts.comments.repository.CommentsHierarchyRepository;
import me.diarity.diaritybespring.posts.comments.repository.CommentsRepository;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentRepository;
    private final CommentsHierarchyRepository commentsHierarchyRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    public CommentsResponse create(CommentsCreateRequest commentsCreateRequest, String email, Long postId) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Comments comment = commentRepository.save(CommentsMapper.INSTANCE.toEntity(
                commentsCreateRequest,
                user,
                post
        ));
        Long parentCommentId = commentsCreateRequest.getParentCommentId();
        if (parentCommentId != null) {
            Comments parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
            commentsHierarchyRepository.save(
                    CommentsHierarchy.builder()
                            .parentComment(parentComment)
                            .childComment(comment)
                            .build());
        }

        CommentsResponse commentsResponse = CommentsMapper.INSTANCE.toResponse(comment);
        commentsResponse.setParentCommentId(parentCommentId);
        return commentsResponse;
    }
}
