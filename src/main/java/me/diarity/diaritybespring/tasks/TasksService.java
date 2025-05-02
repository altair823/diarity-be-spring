package me.diarity.diaritybespring.tasks;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.tasks.dto.TasksCreateRequest;
import me.diarity.diaritybespring.tasks.dto.TasksMapper;
import me.diarity.diaritybespring.tasks.dto.TasksResponse;
import me.diarity.diaritybespring.tasks.entity.Tasks;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepository tasksRepository;
    private final UsersService usersService;

    public TasksResponse create(TasksCreateRequest tasksCreateRequest, String userEmail) {
        Users user = usersService.findEntityByEmail(userEmail);
        Tasks tasks = Tasks.builder()
                .user(user)
                .createdAt(Instant.now())
                .title(tasksCreateRequest.getTitle())
                .description(tasksCreateRequest.getDescription())
                .isCompleted(false)
                .duration(0L)
                .build();
        Tasks resultTask = tasksRepository.save(tasks);
        return TasksMapper.INSTANCE.toTasksResponse(resultTask);
    }


    public List<TasksResponse> findAll(String userEmail) {
        Users user = usersService.findEntityByEmail(userEmail);
        List<Tasks> tasks = tasksRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        return tasks.stream().map(TasksMapper.INSTANCE::toTasksResponse).toList();
    }
}
