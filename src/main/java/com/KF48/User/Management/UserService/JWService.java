package com.KF48.User.Management.UserService;

import com.KF48.User.Management.UserModel.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
public interface JWService {


    String extractUserName(String token); // Not static — to be implemented


    boolean isTokenValid(String token, UserDetails userDetails);


    String generateToken(UserDetails userDetails);



    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
