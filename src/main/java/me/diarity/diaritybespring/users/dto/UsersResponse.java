package me.diarity.diaritybespring.users.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UsersResponse {
    @NonNull
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String picture;
    @NonNull
    private String role;
    @NonNull
    private String displayName;
}
