package com.agridence.microservice.Assignment.security;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtilTest extends JwtUtil {
    @Override
    public String generateToken(UserDetails userDetails) {
        return "test-token-" + userDetails.getUsername();
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        return token.equals("test-token-" + userDetails.getUsername());
    }

    @Override
    public String extractUsername(String token) {
        return token.substring(11); // Remove "test-token-" prefix
    }
}