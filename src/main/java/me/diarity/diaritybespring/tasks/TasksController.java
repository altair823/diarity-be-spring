package me.diarity.diaritybespring.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TasksController {
    private final TasksService tasksService;
    @GetMapping
    public String getTasks() {
        return "Tasks";
    }
}
