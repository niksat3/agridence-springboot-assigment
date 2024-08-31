package com.agridence.microservice.Assignment.service;

import com.agridence.microservice.Assignment.dto.UserDTO;
import com.agridence.microservice.Assignment.dto.param.LoginDTO;
import com.agridence.microservice.Assignment.model.User;
import com.agridence.microservice.Assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setFullName("Test User");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setFullName("Test User");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(savedUser);

        User result = userService.registerUser(userDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("Test User", result.getFullName());

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any());
    }

    @Test
    void login_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(user, null));

        String result = userService.login(loginDTO);

        assertNotNull(result);
        assertEquals("test-token-testuser", result);

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_Failure() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid username/password"));

        assertThrows(BadCredentialsException.class, () -> userService.login(loginDTO));

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loadUserByUsername_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));

        verify(userRepository).findByUsername("nonexistent");
    }
}