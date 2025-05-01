package com.KF48.User.Management.UserRepository;

import com.KF48.User.Management.UserModel.Role;
import com.KF48.User.Management.UserModel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
