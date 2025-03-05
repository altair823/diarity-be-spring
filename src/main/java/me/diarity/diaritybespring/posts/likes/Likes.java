package me.diarity.diaritybespring.posts.likes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.users.Users;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {
    @EmbeddedId
    private LikesId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Posts post;

    @CreationTimestamp
    private Instant createdAt;
}
