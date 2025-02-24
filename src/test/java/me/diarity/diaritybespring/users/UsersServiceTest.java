package me.diarity.diaritybespring.users;

import me.diarity.diaritybespring.users.dto.UsersResponse;
import me.diarity.diaritybespring.users.dto.UsersSaveRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    @Test
    public void findById() {
        // given
        Long id = 1L;
        Users user = Users.builder()
                .id(id)
                .email("testemail@gmail.com")
                .name("testUser")
                .picture("testPicture")
                .role("NORMAL")
                .displayName("testUser")
                .build();
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        UsersResponse usersResponse = usersService.findById(id);

        // then
        assertThat(usersResponse.getId()).isEqualTo(id);
        assertThat(usersResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(user.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo(user.getRole());
        assertThat(usersResponse.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findByIdFail() {
        // given
        Long id = 1L;
        when(usersRepository.findById(id)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> usersService.findById(id));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void findByEmail() {
        // given
        String email = "testemail@gmail.com";
        Users user = Users.builder()
                .id(1L)
                .email(email)
                .name("testUser")
                .picture("testPicture")
                .role("NORMAL")
                .displayName("testUser")
                .build();
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        UsersResponse usersResponse = usersService.findByEmail(email);

        // then
        assertThat(usersResponse.getId()).isEqualTo(user.getId());
        assertThat(usersResponse.getEmail()).isEqualTo(email);
        assertThat(usersResponse.getName()).isEqualTo(user.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo(user.getRole());
        assertThat(usersResponse.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findByEmailFail() {
        // given
        String email = "testemail2@gmail.com";
        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> usersService.findByEmail(email));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void save() {
        // given
        UsersSaveRequest usersSaveRequest = UsersSaveRequest.builder()
                .email("testemail@gmail.com")
                .name("testUser")
                .picture("testPicture")
                .build();
        when(usersRepository.save(any())).thenReturn(
                Users.builder()
                        .id(1L)
                        .email(usersSaveRequest.getEmail())
                        .name(usersSaveRequest.getName())
                        .picture(usersSaveRequest.getPicture())
                        .role("NORMAL")
                        .displayName(usersSaveRequest.getName())
                        .build()
        );

        // when
        UsersResponse usersResponse = usersService.save(usersSaveRequest);

        // then
        assertThat(usersResponse.getId()).isEqualTo(1L);
        assertThat(usersResponse.getEmail()).isEqualTo(usersSaveRequest.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(usersSaveRequest.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(usersSaveRequest.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo("NORMAL");
        assertThat(usersResponse.getDisplayName()).isEqualTo(usersSaveRequest.getName());
    }
}
