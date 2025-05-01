package com.KF48.User.Management.UserService;

import com.KF48.User.Management.UserModel.User;
import com.KF48.User.Management.dto.JwtAuthenticationResponse;
import com.KF48.User.Management.dto.RefreshTokenRequest;
import com.KF48.User.Management.dto.SignUpRequest;
import com.KF48.User.Management.dto.SigninRequest;
import org.springframework.stereotype.Service;


public interface AuthenticationService  {
    User signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
