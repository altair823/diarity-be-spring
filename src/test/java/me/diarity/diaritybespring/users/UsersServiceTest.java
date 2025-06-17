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

    private final Long id = 1L;
    private final Users user = Users.builder()
            .id(id)
            .email("testemail@gmail.com")
            .name("testUser")
            .picture("testPicture")
            .role(UsersRole.READ_ONLY)
            .displayName("testUser")
            .build();

    @Test
    public void findById() {
        // given
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        UsersResponse usersResponse = usersService.findById(id);

        // then
        assertThat(usersResponse.getId()).isEqualTo(id);
        assertThat(usersResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(user.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo(user.getRole().toString());
        assertThat(usersResponse.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findByIdFail() {
        // given
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
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        UsersResponse usersResponse = usersService.findByEmail(user.getEmail());

        // then
        assertThat(usersResponse.getId()).isEqualTo(user.getId());
        assertThat(usersResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(usersResponse.getName()).isEqualTo(user.getName());
        assertThat(usersResponse.getPicture()).isEqualTo(user.getPicture());
        assertThat(usersResponse.getRole()).isEqualTo(user.getRole().toString());
        assertThat(usersResponse.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findByEmailFail() {
        // given
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> usersService.findByEmail(user.getEmail()));
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
                        .role(UsersRole.READ_ONLY)
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
        assertThat(usersResponse.getRole()).isEqualTo("READ_ONLY");
        assertThat(usersResponse.getDisplayName()).isEqualTo(usersSaveRequest.getName());
    }

    @Test
    public void findEntityById() {
        // given
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        Users users = usersService.findEntityById(id);

        // then
        assertThat(users.getId()).isEqualTo(id);
        assertThat(users.getEmail()).isEqualTo(user.getEmail());
        assertThat(users.getName()).isEqualTo(user.getName());
        assertThat(users.getPicture()).isEqualTo(user.getPicture());
        assertThat(users.getRole()).isEqualTo(user.getRole());
        assertThat(users.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findEntityByIdFail() {
        // given
        when(usersRepository.findById(id)).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> usersService.findEntityById(id));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }

    @Test
    public void findEntityByEmail() {
        // given
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        Users users = usersService.findEntityByEmail(user.getEmail());

        // then
        assertThat(users.getId()).isEqualTo(user.getId());
        assertThat(users.getEmail()).isEqualTo(user.getEmail());
        assertThat(users.getName()).isEqualTo(user.getName());
        assertThat(users.getPicture()).isEqualTo(user.getPicture());
        assertThat(users.getRole()).isEqualTo(user.getRole());
        assertThat(users.getDisplayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    public void findEntityByEmailFail() {
        // given
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        // throws IllegalArgumentException
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> usersService.findEntityByEmail(user.getEmail()));
        assertThat(e.getMessage()).isEqualTo("해당 사용자가 없습니다.");
    }
}
