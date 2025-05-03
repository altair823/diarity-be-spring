package me.diarity.diaritybespring.auth.dto;

import lombok.Data;

@Data
public class GoogleLoginUserInfoResponse {
    private String id;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
}
