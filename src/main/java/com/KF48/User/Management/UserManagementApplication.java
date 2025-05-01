package com.KF48.User.Management;

import com.KF48.User.Management.UserModel.Role;
import com.KF48.User.Management.UserModel.User;
import com.KF48.User.Management.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan(basePackages = "com.KF48.User.Management")
@SpringBootApplication
public class UserManagementApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

	  public void run(String... args){
		  User adminAccount = userRepository.findByRole(Role.ADMIN);
		  if (null == adminAccount){
			  User user = new User();
			  user.setEmail("admin@gmail.com");
			  user.setFirstname("adim");
			  user.setSecondname("adim");
			  user.setRole(Role.ADMIN);
			  user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			  userRepository.save(user);
		  }

	  }

}
