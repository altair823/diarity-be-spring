package me.diarity.diaritybespring.auth.dto;

import lombok.Builder;
import lombok.Data;
import me.diarity.diaritybespring.users.dto.UsersResponse;

@Data
@Builder
public class AuthResponse {
    private String status;
    private UsersResponse user;
    private String version;
}
