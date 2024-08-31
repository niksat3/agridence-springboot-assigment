package com.agridence.microservice.Assignment.controller;

import com.agridence.microservice.Assignment.dto.UserDTO;
import com.agridence.microservice.Assignment.dto.param.LoginDTO;
import com.agridence.microservice.Assignment.model.User;
import com.agridence.microservice.Assignment.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User management APIs")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<User> signUp(@RequestBody UserDTO userDTO) {
        User registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            return ResponseEntity.ok(new AuthenticationResponse(token, "Login successful"));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid username/password"));
        }
    }
}

class AuthenticationResponse {
    private final String jwt;
    private final String message;

    public AuthenticationResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }
}

class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
