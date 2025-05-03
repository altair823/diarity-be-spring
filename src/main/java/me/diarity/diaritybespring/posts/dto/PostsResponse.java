package me.diarity.diaritybespring.posts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import me.diarity.diaritybespring.users.dto.UsersResponse;

import java.time.LocalDateTime;

@Data
@Builder
@Setter
public class PostsResponse {
    @NonNull
    private Long id;
    @NonNull
    private String bookTitle;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private UsersResponse author;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;
    @NonNull
    private Boolean isPublic;
    @NonNull
    private Boolean isDeleted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    @NonNull
    private Integer likesCount;
    @NonNull
    private Integer commentsCount;
    private Boolean isLiked;
}
