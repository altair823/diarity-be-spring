package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.dto.TasksCreateRequest;
import me.diarity.diaritybespring.tasks.dto.TasksResponse;
import me.diarity.diaritybespring.tasks.entity.Tasks;
import me.diarity.diaritybespring.users.Users;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@SpringBootTest
public class TasksServiceTest {
    @Mock
    private TasksRepository tasksRepository;

    @InjectMocks
    private TasksService tasksService;

    private final Users user = Users.builder()
            .id(1L)
            .name("Test User")
            .email("testEmail@gmail.com")
            .picture("testPicture")
            .role("NORMAL")
            .displayName("Test Display Name")
            .build();

    private final Tasks task = Tasks.builder()
            .id(1L)
            .user(user)
            .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0).toInstant(ZoneOffset.of("+09:00")))
            .title("Test Task")
            .description("Test Description")
            .isCompleted(false)
            .duration(60L)
            .build();

    private final TasksResponse tasksResponse = TasksResponse.builder()
            .id(task.getId())
            .userEmail(user.getEmail())
            .createdAt(task.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
            .title(task.getTitle())
            .description(task.getDescription())
            .isCompleted(task.getIsCompleted())
            .duration(task.getDuration())
            .build();

    @Test
    public void create() {
        // given
        TasksCreateRequest tasksCreateRequest = TasksCreateRequest.builder()
                .userEmail(user.getEmail())
                .title("Test Task")
                .description("Test Description")
                .build();

        System.out.println(tasksResponse.getCreatedAt());

    }
}
