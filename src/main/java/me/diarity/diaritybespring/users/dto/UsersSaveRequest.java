package me.diarity.diaritybespring.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UsersSaveRequest {
    private String name;
    private String email;
    private String picture;
    private String role;
    private String displayName;
}
