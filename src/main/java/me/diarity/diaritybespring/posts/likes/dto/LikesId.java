package me.diarity.diaritybespring.posts.likes.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikesId implements Serializable {
    private Long postId;
    private Long userId;
}
