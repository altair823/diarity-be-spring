package me.diarity.diaritybespring.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersResponse {
    private Long id;
    private String email;
    private String name;
    private String picture;
    private String role;
    private String displayName;
}
