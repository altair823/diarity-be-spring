package me.diarity.diaritybespring.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginUserInfoResponse {
    private String email;
    private String name;
    private String picture;
}
