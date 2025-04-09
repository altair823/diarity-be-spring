package me.diarity.diaritybespring.tasks.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TasksResponse {
    private Long id;
    private String userEmail;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private Boolean isCompleted;
    private Long duration;
}
