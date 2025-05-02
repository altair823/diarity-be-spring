package me.diarity.diaritybespring.posts.dto;

import lombok.*;

@Data
@Builder
@Getter
public class PostsCreateRequest {
    @NonNull
    private String bookTitle;

    @NonNull
    private String title;

    @NonNull
    private String content;
}
