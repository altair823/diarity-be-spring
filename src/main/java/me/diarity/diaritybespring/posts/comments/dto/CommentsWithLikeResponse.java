package me.diarity.diaritybespring.posts.comments.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentsWithLikeResponse {
    private Long id;
    private Long authorUserId;
    private String authorDisplayName;
    private String authorPicture;
    private Instant createdAt;
    private Instant modifiedAt;
    private String content;
    private Boolean isDeleted;
    private Instant deletedAt;
    private Integer likesCount;
    private Long postId;
    private Long parentCommentId;
    private Boolean isLiked;
}
