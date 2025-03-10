package me.diarity.diaritybespring.posts.comments.entity;

import jakarta.persistence.*;
import lombok.*;
import me.diarity.diaritybespring.posts.Posts;
import me.diarity.diaritybespring.users.Users;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users author;

    @ManyToOne
    private Posts post;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant modifiedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isDeleted;
    private Instant deletedAt;
    private Integer likesCount;

    public void addLike() {
        this.likesCount++;
    }

    public void removeLike() {
        this.likesCount--;
    }
}
