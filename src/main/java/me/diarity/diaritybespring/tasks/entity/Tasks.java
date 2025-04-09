package me.diarity.diaritybespring.tasks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.diarity.diaritybespring.users.Users;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    @CreatedDate
    private Instant createdAt;

    private String title;
    private String description;
    private Boolean isCompleted;
    private Long duration;

}
