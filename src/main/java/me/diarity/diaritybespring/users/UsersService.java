package me.diarity.diaritybespring.users;

import lombok.RequiredArgsConstructor;
import me.diarity.diaritybespring.posts.PostsService;
import me.diarity.diaritybespring.posts.comments.CommentsService;
import me.diarity.diaritybespring.posts.dto.PostsResponse;
import me.diarity.diaritybespring.users.dto.UsersMapper;
import me.diarity.diaritybespring.users.dto.UsersProfileResponse;
import me.diarity.diaritybespring.users.dto.UsersResponse;
import me.diarity.diaritybespring.users.dto.UsersSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final PostsService postsService;
    private final CommentsService commentsService;

    public UsersResponse findById(Long id) {
        Users user = findEntityById(id);
        return UsersMapper.INSTANCE.toResponse(user);
    }

    public UsersResponse findByEmail(String email) {
        Users user = findEntityByEmail(email);
        return UsersMapper.INSTANCE.toResponse(user);
    }

    @Transactional
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

    public UsersProfileResponse getProfile(Long userId) {
        Users user = findEntityById(userId);
        int PostsCount = postsService.countByUserId(user.getId());
        int CommentsCount = commentsService.countCommentsByUserId(user.getId());
        return UsersProfileResponse.builder()
                .usersInfo(UsersMapper.INSTANCE.toResponse(user))
                .postsCount(PostsCount)
                .commentsCount(CommentsCount)
                .build();
    }

    public List<PostsResponse> findPostsByUserId(Long userId, Integer count) {
        if (count == null) {
            Users user = findEntityById(userId);
            return postsService.findAllByUser(user);
        } else {
            Users user = findEntityById(userId);
            return postsService.findByUserLimitCount(user, count);
        }
    }
}
