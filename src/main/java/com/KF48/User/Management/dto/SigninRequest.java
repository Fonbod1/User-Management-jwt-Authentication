package com.KF48.User.Management.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

public class SigninRequest {
    private String email;

    private String password;

}
