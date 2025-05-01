package com.KF48.User.Management.UserService.Implementation;
//import com.KF48.User.Management.Repository.UserRepository;
//import com.KF48.User.Management.Security.Jwt.JWService;
import com.KF48.User.Management.UserModel.User;
import com.KF48.User.Management.UserRepository.UserRepository;
import com.KF48.User.Management.UserService.JWService;
import com.KF48.User.Management.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWService jwService;

    @Override
    public UserDetailsService userDetailsService() {
        return this::loadUserByEmail;
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return jwService.generateRefreshToken(extraClaims, userDetails);
    }

    // ✅ Implement the new methods below:

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setSecondname(updatedUser.getSecondname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword()); // encode if necessary
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
