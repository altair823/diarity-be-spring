package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.dto.TasksCreateRequest;
import me.diarity.diaritybespring.tasks.dto.TasksResponse;
import me.diarity.diaritybespring.tasks.entity.Tasks;
import me.diarity.diaritybespring.users.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TasksControllerTest {
    @Mock
    private TasksService tasksService;

    @InjectMocks
    private TasksController tasksController;

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
            .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC))
            .title("Test Task")
            .description("Test Description")
            .isCompleted(false)
            .duration(60L)
            .build();

    @BeforeEach
    public void setUp() {
        // Create a mock Authentication object
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user.getEmail());

        // Create a mock SecurityContext object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void create() {
        // Given
        TasksCreateRequest tasksCreateRequest = TasksCreateRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .build();
        TasksResponse tasksResponse = TasksResponse.builder()
                .id(task.getId())
                .userEmail(user.getEmail())
                .createdAt(LocalDateTime.ofInstant(task.getCreatedAt(), ZoneId.systemDefault()))
                .title(task.getTitle())
                .description(task.getDescription())
                .isCompleted(task.getIsCompleted())
                .duration(task.getDuration())
                .build();
        Mockito.when(tasksService.create(tasksCreateRequest, user.getEmail())).thenReturn(tasksResponse);

        // When
        TasksResponse result = tasksController.create(tasksCreateRequest);

        // Then
        assertThat(result.getId()).isEqualTo(task.getId());
        assertThat(result.getUserEmail()).isEqualTo(user.getEmail());
        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(task.getCreatedAt(), ZoneId.systemDefault()));
        assertThat(result.getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getDescription()).isEqualTo(task.getDescription());
        assertThat(result.getIsCompleted()).isEqualTo(task.getIsCompleted());
        assertThat(result.getDuration()).isEqualTo(task.getDuration());
    }

    @Test
    public void getAll() {
        // Given
        TasksResponse tasksResponse = TasksResponse.builder()
                .id(task.getId())
                .userEmail(user.getEmail())
                .createdAt(LocalDateTime.ofInstant(task.getCreatedAt(), ZoneId.systemDefault()))
                .title(task.getTitle())
                .description(task.getDescription())
                .isCompleted(task.getIsCompleted())
                .duration(task.getDuration())
                .build();
        Mockito.when(tasksService.findAll(user.getEmail())).thenReturn(List.of(tasksResponse));

        // When
        List<TasksResponse> result = tasksController.getAll();

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId()).isEqualTo(task.getId());
        assertThat(result.getFirst().getUserEmail()).isEqualTo(user.getEmail());
        assertThat(result.getFirst().getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(task.getCreatedAt(), ZoneId.systemDefault()));
        assertThat(result.getFirst().getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getFirst().getDescription()).isEqualTo(task.getDescription());
        assertThat(result.getFirst().getIsCompleted()).isEqualTo(task.getIsCompleted());
        assertThat(result.getFirst().getDuration()).isEqualTo(task.getDuration());
    }
}