package com.KF48.User.Management.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;

    private String refreshToken;
}
