package me.diarity.diaritybespring.posts.likes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikesResponse {
    private Long postId;
    private Long userId;
}
