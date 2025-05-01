package com.KF48.User.Management.UserController;

import com.KF48.User.Management.UserModel.User;
import com.KF48.User.Management.UserService.AuthenticationService;
import com.KF48.User.Management.dto.JwtAuthenticationResponse;
import com.KF48.User.Management.dto.RefreshTokenRequest;
import com.KF48.User.Management.dto.SignUpRequest;
import com.KF48.User.Management.dto.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
private final AuthenticationService authenticationService;
@PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
    return ResponseEntity.ok(authenticationService.signUp(signUpRequest));

}

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }


}
