package com.example.To_Do_API.services;

import com.example.To_Do_API.dto.response.AuthResponse;
import com.example.To_Do_API.model.User;
import com.example.To_Do_API.repository.UserRepository;
import com.example.To_Do_API.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User Registration
    public AuthResponse userRegister(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail())
                .or(() -> userRepository.findByUsername(user.getUsername()));

        if (existingUser.isPresent()) {
            String errorMessage = existingUser.get().getEmail().equals(user.getEmail()) ? "Email already exists" : "Username already exists";
            return new AuthResponse(null, null, errorMessage, false);
        }


        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new AuthResponse(null, user.getUsername(), "Registration successful", true);
    }

    // User Login
    public AuthResponse userLogin(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);


        if (user.isPresent()) {

            if (passwordEncoder.matches(password, user.get().getPassword())) {
                // Generate JWT token using email
                String token = jwtUtil.generateToken(user.get().getEmail());

                return new AuthResponse(token, user.get().getUsername(), "Login successful", true);
            } else {
                return new AuthResponse(null, null, "Invalid password", false);
            }

        } else {
            return new AuthResponse(null, null, "Invalid email", false);
        }

    }
}
