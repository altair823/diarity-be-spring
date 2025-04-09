package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.entity.Tasks;
import me.diarity.diaritybespring.users.Users;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void create() {

    }
}