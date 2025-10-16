package com.ema.api.service;

import com.ema.api.model.User;
import com.ema.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private User driver;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        driver = new User();
        driver.setId(2L);
        driver.setName("Test Driver");
        driver.setEmail("driver@example.com");
        driver.setPassword("password");
        driver.setRole("DRIVER");
        driver.setStatus("AVAILABLE");
    }

    @Test
    void whenCreateUser_thenUserIsCreated() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.createUser(user);

        // Then
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    void whenGetAllUsers_thenReturnListOfUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, driver));

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertEquals(2, users.size());
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userService.getUserById(1L);

        // Then
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    void whenUpdateUser_thenUserIsUpdated() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userDetails = new User();
        userDetails.setName("Updated Name");
        userDetails.setEmail("updated@example.com");
        userDetails.setPassword(""); // Not updating password

        // When
        User updatedUser = userService.updateUser(1L, userDetails);

        // Then
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenGetAvailableDrivers_thenReturnListOfAvailableDrivers() {
        // Given
        when(userRepository.findByRoleAndStatus("DRIVER", "AVAILABLE")).thenReturn(Arrays.asList(driver));

        // When
        List<User> drivers = userService.getAvailableDrivers();

        // Then
        assertEquals(1, drivers.size());
        assertEquals("DRIVER", drivers.get(0).getRole());
        assertEquals("AVAILABLE", drivers.get(0).getStatus());
    }
}
