package me.diarity.diaritybespring.tasks;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.tasks.dto.TasksCreateRequest;
import me.diarity.diaritybespring.tasks.dto.TasksResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TasksController {
    private final TasksService tasksService;

    @PostMapping
    public TasksResponse create(@RequestBody TasksCreateRequest tasksCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getPrincipal().toString();
        return tasksService.create(tasksCreateRequest, userEmail);
    }

    @GetMapping
    public List<TasksResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getPrincipal().toString();
        return tasksService.findAll(userEmail);
    }
}
