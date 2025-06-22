package me.diarity.diaritybespring.users;

import me.diarity.diaritybespring.users.dto.UsersProfileResponse;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import me.diarity.diaritybespring.utils.UsersContextHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsersControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @Test
    void getUserProfile() {
        // Given
        String userEmail = "testEmail@gmail.com";
        UsersProfileResponse expectedResponse = UsersProfileResponse.builder()
                .usersInfo(UsersResponse.builder()
                        .id(1L)
                        .email(userEmail)
                        .name("Test User")
                        .picture("testPictureUrl")
                        .role("READ_ONLY")
                        .displayName("Test User")
                        .build())
                .postsCount(5)
                .commentsCount(10)
                .build();
        try (MockedStatic<UsersContextHandler> mockedStatic = mockStatic(UsersContextHandler.class)) {
            mockedStatic.when(UsersContextHandler::getUserEmail).thenReturn(userEmail);
            when(usersService.getProfile(expectedResponse.getUsersInfo().getId())).thenReturn(expectedResponse);

            // When
            UsersProfileResponse actualResponse = usersController.getUserProfile(expectedResponse.getUsersInfo().getId());

            // Then
            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);
            verify(usersService).getProfile(expectedResponse.getUsersInfo().getId());
        }
    }
}