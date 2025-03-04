package me.diarity.diaritybespring.posts;

import jakarta.persistence.*;
import lombok.*;
import me.diarity.diaritybespring.users.Users;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @CreatedBy
    @ManyToOne
    private Users author;

    @CreatedDate
    private Instant createdAt;

    @UpdateTimestamp
    private Instant modifiedAt;

    private Boolean isPublic;
    private Boolean isDeleted;

    private Instant deletedAt;
    private Integer likesCount;
    private Integer commentsCount;
}
