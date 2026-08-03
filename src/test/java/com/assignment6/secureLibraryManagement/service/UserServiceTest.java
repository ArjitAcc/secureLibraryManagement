package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.UserRequestJO;
import com.assignment6.secureLibraryManagement.dto.UserResponseJO;
import com.assignment6.secureLibraryManagement.entity.Role;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    Authentication authentication;
    @Mock
    GrantedAuthority authority;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void addUserShouldAddUserSuccessfully() {
        UserRequestJO userRequest = new UserRequestJO("user", "email@domain.com", "address", "password", Role.USER, true);
        User savedUser = new User(userRequest.name(), userRequest.emailAddress(), userRequest.address(), userRequest.role(), userRequest.password(), userRequest.isActive());
        Long userId = 101L;
        savedUser.setId(userId);
        Mockito.when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn("password");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        Long returnedId = userService.addUser(userRequest);
        assertEquals(userId, returnedId);
    }

    @Test
    void updateUserShouldUpdateUserSuccessfully() {
        UserRequestJO userRequest = new UserRequestJO("userUpdated", "emailUpdated@domain.com", "addressUpdated", "passwordUpdated", Role.USER, false);
        Long userId = 101L;
        User existingUser = new User("user", "email@domain.com", "address", Role.USER, "password", true);
        existingUser.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("USER"));
        Mockito.doReturn(authorities)
                .when(authentication)
                .getAuthorities();
        Mockito.when(authentication.getName()).thenReturn("email@domain.com");
//        Mockito.doNothing().when(userRepository).save(Mockito.any(User.class));
        User updatedUser = new User(userRequest.name(), userRequest.emailAddress(), userRequest.address(), userRequest.role(), userRequest.password(), userRequest.isActive());
        updatedUser.setId(userId);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);
        userService.updateUser(userRequest, userId, authentication);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void getUserShouldGetUserSuccessfully() {
        Long userId = 101L;
        User existingUser = new User("user", "email@domain.com", "address", Role.USER, "password", true);
        existingUser.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("USER"));
        Mockito.doReturn(authorities)
                .when(authentication)
                .getAuthorities();
        Mockito.when(authentication.getName()).thenReturn("email@domain.com");
        UserResponseJO userReturned = userService.getUser(userId, authentication);
        assertEquals(existingUser.getEmailAddress(), userReturned.emailAddress());
        assertEquals(existingUser.getAddress(), userReturned.address());
        assertEquals(existingUser.getName(), userReturned.name());
        assertEquals(existingUser.getRole(), userReturned.role());
    }

    @Test
    void softDelete() {
        Long userId = 101L;
        User existingUser = new User("user", "email@domain.com", "address", Role.USER, "password", true);
        existingUser.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("USER"));
        Mockito.doReturn(authorities)
                .when(authentication)
                .getAuthorities();
        Mockito.when(authentication.getName()).thenReturn("email@domain.com");
        existingUser.setActive(false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(existingUser);
        userService.softDelete(userId, authentication);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void setActive() {
        Long userId = 101L;
        User existingUser = new User("user", "email@domain.com", "address", Role.USER, "password", true);
        existingUser.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("USER"));
        Mockito.doReturn(authorities)
                .when(authentication)
                .getAuthorities();
        Mockito.when(authentication.getName()).thenReturn("email@domain.com");
        existingUser.setActive(false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(existingUser);
        userService.softDelete(userId, authentication);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }
}