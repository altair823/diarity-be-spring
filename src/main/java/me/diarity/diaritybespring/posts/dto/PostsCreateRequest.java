package me.diarity.diaritybespring.posts.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@Builder
@Getter
public class PostsCreateRequest {
    @NonNull
    private String title;
    @NonNull
    private String content;
}
