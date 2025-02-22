package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.users.dto.UsersMapper;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersResponse findById(Long id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return UsersMapper.INSTANCE.toResponse(user);
    }

    public UsersResponse findByEmail(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return UsersMapper.INSTANCE.toResponse(user);
    }
}
