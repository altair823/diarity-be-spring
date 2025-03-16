package me.diarity.diaritybespring.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PostsWithLikeResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Instant createdAt;
    private Instant modifiedAt;
    private Boolean isPublic;
    private Boolean isDeleted;
    private Instant deletedAt;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean isLiked;
}
