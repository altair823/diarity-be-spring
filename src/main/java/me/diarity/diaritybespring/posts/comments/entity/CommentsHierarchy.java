package me.diarity.diaritybespring.posts.comments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.diarity.diaritybespring.posts.comments.dto.CommentsHierarchyId;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsHierarchy {
    @EmbeddedId
    private CommentsHierarchyId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentCommentId")
    private Comments parentComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("childCommentId")
    private Comments childComments;
}
