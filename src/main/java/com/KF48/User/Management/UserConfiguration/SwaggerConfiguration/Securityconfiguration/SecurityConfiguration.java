package com.KF48.User.Management.UserConfiguration.SwaggerConfiguration.Securityconfiguration;//package com.Kpackage com.KF48.User.Management.UserConfiguration.SwaggerConfiguration.Securityconfiguration;

import com.KF48.User.Management.UserConfiguration.SwaggerConfiguration.Securityconfiguration.JwtAuthenticationFilter;
import com.KF48.User.Management.UserService.UserService;
import com.KF48.User.Management.UserModel.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    //private final com.KF48.User.Management.UserConfiguration.SwaggerConfiguration.Security.JwtAuthenticationFilter jwtAuthenticationFilter;  // Ensure this is a Spring Bean (@Component or @Service)
    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()  // Using `authorizeRequests()` instead of `authorizeHttpRequests()`
                .requestMatchers("/api/v1/auth/**").permitAll()  // Allow authentication routes without authentication
                .requestMatchers("/api/v1/admin").hasAuthority(Role.ADMIN.name()) // Require ROLE_ADMIN for /admin
                .requestMatchers("/api/v1/user").hasAuthority(Role.USER.name()) // Require ROLE_USER for /user
                .anyRequest().authenticated()  // Require authentication for other routes
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService.userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}