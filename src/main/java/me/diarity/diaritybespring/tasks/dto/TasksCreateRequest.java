package me.diarity.diaritybespring.tasks.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TasksCreateRequest {
    private String title;
    private String description;
}
