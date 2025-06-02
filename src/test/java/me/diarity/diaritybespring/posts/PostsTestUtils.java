package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsResponse;

import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostsTestUtils {

    public static void assertPostsResponse(PostsResponse postsResponse, Posts post) {
        assertThat(postsResponse.getTitle()).isEqualTo(post.getTitle());
        assertThat(postsResponse.getContent()).isEqualTo(post.getContent());
        assertThat(postsResponse.getAuthor().getEmail()).isEqualTo(post.getAuthor().getEmail());
        assertThat(postsResponse.getCreatedAt()).isEqualTo(post.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getModifiedAt()).isEqualTo(post.getModifiedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
        assertThat(postsResponse.getIsPublic()).isEqualTo(post.getIsPublic());
        assertThat(postsResponse.getIsDeleted()).isEqualTo(post.getIsDeleted());
        assertThat(postsResponse.getDeletedAt()).isEqualTo(post.getDeletedAt());
        assertThat(postsResponse.getLikesCount()).isEqualTo(post.getLikesCount());
        assertThat(postsResponse.getCommentsCount()).isEqualTo(post.getCommentsCount());
    }
}
