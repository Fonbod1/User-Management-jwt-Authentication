package com.KF48.User.Management.UserService.Implementation;

import com.KF48.User.Management.UserModel.Role;
import com.KF48.User.Management.UserModel.User;
import com.KF48.User.Management.UserRepository.UserRepository;
import com.KF48.User.Management.UserService.AuthenticationService;
import com.KF48.User.Management.dto.JwtAuthenticationResponse;
import com.KF48.User.Management.dto.RefreshTokenRequest;
import com.KF48.User.Management.dto.SignUpRequest;
import com.KF48.User.Management.dto.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTserviceImpl jwTservice;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstname(signUpRequest.getFirstname());
        user.setSecondname(signUpRequest.getLastname());
        user.setRole(Role.USER); // You can make this dynamic if needed
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        var user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var jwt = jwTservice.generateToken(user);
        var refreshToken = jwTservice.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwTservice.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (jwTservice.isTokenValid(refreshTokenRequest.getToken(), user));
        var jwt = jwTservice.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
        return jwtAuthenticationResponse;

    }

}
