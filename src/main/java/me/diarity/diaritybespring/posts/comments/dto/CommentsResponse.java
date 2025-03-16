package me.diarity.diaritybespring.posts.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Builder
@Setter
public class CommentsResponse {
    @NonNull
    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private String displayName;
    @NonNull
    private String picture;
    @NonNull
    private String content;
    @NonNull
    private Integer likesCount;
    @NonNull
    private Boolean isLiked;
    @NonNull
    private Long postId;
    private Long parentCommentId;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;
}
