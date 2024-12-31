package com.example.To_Do_API.controller;

import com.example.To_Do_API.dto.request.LoginRequest;
import com.example.To_Do_API.dto.response.AuthResponse;
import com.example.To_Do_API.model.User;
import com.example.To_Do_API.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) {
        AuthResponse response = userService.userRegister(user);

        if (response.isSuccess()) {

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } else if ("Email already exists".equals(response.getMessage())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } else if ("Username already exists".equals(response.getMessage())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = userService.userLogin(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else if ("Invalid password".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
