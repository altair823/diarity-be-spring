package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.users.dto.UsersMapper;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import me.diarity.diaritybespring.users.dto.UsersSaveRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersResponse findById(Long id) {
        Users user = findEntityById(id);
        return UsersMapper.INSTANCE.toResponse(user);
    }

    public UsersResponse findByEmail(String email) {
        Users user = findEntityByEmail(email);
        return UsersMapper.INSTANCE.toResponse(user);
    }

    public UsersResponse save(UsersSaveRequest usersSaveRequest) {
        Users user = usersRepository.save(UsersMapper.INSTANCE.toEntity(usersSaveRequest));
        return UsersMapper.INSTANCE.toResponse(user);
    }

    public Users findEntityById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    public Users findEntityByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }
}
