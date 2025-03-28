package me.diarity.diaritybespring.posts.likes.dto;

import lombok.Builder;
import lombok.Data;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.users.Users;

@Data
@Builder
public class PostsLikesRequest {
    private Posts post;
    private Users user;
}
