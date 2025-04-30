package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.dto.TasksCreateRequest;
import me.diarity.diaritybespring.tasks.dto.TasksResponse;
import me.diarity.diaritybespring.tasks.entity.Tasks;
import me.diarity.diaritybespring.users.Users;
import me.diarity.diaritybespring.users.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TasksServiceTest {
    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private UsersService usersService;

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
            .createdAt(task.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime())
            .title(task.getTitle())
            .description(task.getDescription())
            .isCompleted(task.getIsCompleted())
            .duration(task.getDuration())
            .build();

    private void assertTasksResponse(TasksResponse tasksResponse, Tasks tasks) {
        assert tasksResponse.getId().equals(tasks.getId());
        assert tasksResponse.getUserEmail().equals(tasks.getUser().getEmail());
        assert tasksResponse.getCreatedAt().equals(tasks.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
        assert tasksResponse.getTitle().equals(tasks.getTitle());
        assert tasksResponse.getDescription().equals(tasks.getDescription());
        assert tasksResponse.getIsCompleted().equals(tasks.getIsCompleted());
        assert tasksResponse.getDuration().equals(tasks.getDuration());
    }

    @Test
    public void create() {
        // given
        TasksCreateRequest tasksCreateRequest = TasksCreateRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .build();
        when(usersService.findEntityByEmail(user.getEmail())).thenReturn(user);
        when(tasksRepository.save(any())).thenReturn(task);
        when(tasksRepository.findById(task.getId())).thenReturn(java.util.Optional.of(task));

        // when
        TasksResponse result = tasksService.create(tasksCreateRequest, user.getEmail());

        // then
        assertTasksResponse(result, task);
    }

    @Test
    public void createFailNotFoundUser() {
        // given
        TasksCreateRequest tasksCreateRequest = TasksCreateRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .build();
        when(usersService.findEntityByEmail(user.getEmail())).thenThrow(new IllegalArgumentException("해당 사용자가 없습니다."));

        // when
        try {
            tasksService.create(tasksCreateRequest, user.getEmail());
        } catch (IllegalArgumentException e) {
            // then
            assert e.getMessage().equals("해당 사용자가 없습니다.");
        }
    }

    @Test
    public void createFailAnonymousUser() {
        // given
        TasksCreateRequest tasksCreateRequest = TasksCreateRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .build();
        String anonymousUserEmail = "anonymousUser";
        when(usersService.findEntityByEmail(anonymousUserEmail)).thenThrow(new IllegalArgumentException("해당 사용자가 없습니다."));

        // when
        try {
            tasksService.create(tasksCreateRequest, anonymousUserEmail);
        } catch (IllegalArgumentException e) {
            // then
            assert e.getMessage().equals("해당 사용자가 없습니다.");
        }
    }

    @Test
    public void findAll() {
        // given
        when(usersService.findEntityByEmail(user.getEmail())).thenReturn(user);
        when(tasksRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())).thenReturn(List.of(task));

        // when
        List<TasksResponse> result = tasksService.findAll(user.getEmail());

        // then
        assert result.size() == 1;
        assertTasksResponse(result.getFirst(), task);
    }
}
