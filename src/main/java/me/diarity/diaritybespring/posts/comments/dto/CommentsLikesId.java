package me.diarity.diaritybespring.posts.comments.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentsLikesId implements Serializable {
    private Long commentId;
    private Long userId;
}
