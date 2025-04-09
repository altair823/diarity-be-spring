package me.diarity.diaritybespring.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TasksService {
    private TasksRepository tasksRepository;
}
