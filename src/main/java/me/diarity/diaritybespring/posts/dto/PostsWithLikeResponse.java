package me.diarity.diaritybespring.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class PostsWithLikeResponse {
    private Long id;
    private String bookTitle;
    private String title;
    private String content;
    private String authorEmail;
    private Instant createdAt;
    private Instant modifiedAt;
    private Boolean isPublic;
    private Boolean isDeleted;
    private Instant deletedAt;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean isLiked;
}
