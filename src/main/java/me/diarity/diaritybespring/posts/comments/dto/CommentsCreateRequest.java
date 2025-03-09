package me.diarity.diaritybespring.posts.comments.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CommentsCreateRequest {
    @NonNull
    private String content;
    private Long parentCommentId;
}
