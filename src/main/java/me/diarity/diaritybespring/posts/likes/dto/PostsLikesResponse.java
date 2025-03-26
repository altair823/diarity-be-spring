package me.diarity.diaritybespring.posts.likes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostsLikesResponse {
    private Long postId;
    private Long userId;
}
