package me.diarity.diaritybespring.auth.dto;

import lombok.Data;

@Data
public class LoginUserInfoResponse {
    private String email;
    private String name;
    private String picture;
}
