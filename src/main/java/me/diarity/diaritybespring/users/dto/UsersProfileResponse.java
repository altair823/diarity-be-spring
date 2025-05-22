package me.diarity.diaritybespring.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersProfileResponse {
    private final UsersResponse usersInfo;
    private final Integer postsCount;
    private final Integer commentsCount;
}
