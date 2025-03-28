package me.diarity.diaritybespring.posts.comments.entity;

import jakarta.persistence.*;
import lombok.*;
import me.diarity.diaritybespring.posts.comments.dto.CommentsLikesId;
import me.diarity.diaritybespring.users.Users;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsLikes {
    @EmbeddedId
    private CommentsLikesId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    private Comments comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private Users user;
}
