package me.diarity.diaritybespring.auth;

import me.diarity.diaritybespring.auth.dto.AuthResponse;
import me.diarity.diaritybespring.auth.dto.LoginUserInfoResponse;
import me.diarity.diaritybespring.users.UsersService;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {
    @Mock
    private UsersService usersService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    public void findOrSaveUser() {
        // given
        LoginUserInfoResponse loginUserInfoResponse = LoginUserInfoResponse.builder()
                .email("testemail@gmail.com")
                .name("testuser")
                .picture("testpictureUrl")
                .build();
        when(usersService.findByEmail(loginUserInfoResponse.getEmail())).thenReturn(
                UsersResponse.builder()
                        .email(loginUserInfoResponse.getEmail())
                        .name(loginUserInfoResponse.getName())
                        .picture(loginUserInfoResponse.getPicture())
                        .role("NORMAL")
                        .displayName(loginUserInfoResponse.getName())
                        .id(1L)
                        .build()
        );

        // when
        UsersResponse usersResponse = authService.findOrSaveUser(loginUserInfoResponse);

        // then
        assertThat(usersResponse.getEmail()).isEqualTo(loginUserInfoResponse.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(loginUserInfoResponse.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(loginUserInfoResponse.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo("NORMAL");
        assertThat(usersResponse.getDisplayName()).isEqualTo(loginUserInfoResponse.getName());
    }

    @Test
    public void findOrSaveUserFailToFind() {
        // given
        LoginUserInfoResponse loginUserInfoResponse = LoginUserInfoResponse.builder()
                .email("testemail@gmail.com")
                .name("testuser")
                .picture("testpictureUrl")
                .build();
        when(usersService.findByEmail(loginUserInfoResponse.getEmail()))
                .thenThrow(new IllegalArgumentException("해당 사용자가 없습니다."));
        when(usersService.save(any())).thenReturn(
                UsersResponse.builder()
                        .email(loginUserInfoResponse.getEmail())
                        .name(loginUserInfoResponse.getName())
                        .picture(loginUserInfoResponse.getPicture())
                        .role("NORMAL")
                        .displayName(loginUserInfoResponse.getName())
                        .id(1L)
                        .build()
        );

        // when
        UsersResponse usersResponse = authService.findOrSaveUser(loginUserInfoResponse);

        // then
        assertThat(usersResponse.getEmail()).isEqualTo(loginUserInfoResponse.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(loginUserInfoResponse.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(loginUserInfoResponse.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo("NORMAL");
        assertThat(usersResponse.getDisplayName()).isEqualTo(loginUserInfoResponse.getName());
    }

    @Test
    public void getStatus() {
        // given
        String accessToken = "testAccessToken";
        UsersResponse usersResponse = UsersResponse.builder()
                .email("testemail@gmail.com")
                .name("testuser")
                .picture("testpictureUrl")
                .role("NORMAL")
                .displayName("testuser")
                .id(1L)
                .build();
        when(jwtUtils.getEmail(any())).thenReturn(usersResponse.getEmail());
        when(usersService.findByEmail(usersResponse.getEmail())).thenReturn(usersResponse);

        // when
        AuthResponse response = authService.getStatus(accessToken);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getUser().getEmail()).isEqualTo(usersResponse.getEmail());
        assertThat(response.getUser().getName()).isEqualTo(usersResponse.getName());
        assertThat(response.getUser().getPicture()).isEqualTo(usersResponse.getPicture());
        assertThat(response.getUser().getRole()).isEqualTo(usersResponse.getRole());
        assertThat(response.getUser().getDisplayName()).isEqualTo(usersResponse.getDisplayName());
    }
}
