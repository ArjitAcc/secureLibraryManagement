package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.Role;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UserNotActiveException;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserValidationService userValidationService;
    @Test
    void validateActiveUserShouldValidateUserSuccessfully(){
        Long id = 1L;
        User user = new User("User1", "email1@email.com", "address1", Role.USER, "password1", true);
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userValidationService.validateUser(id));
    }

    @Test
    void validateInactiveUserShouldValidateUserSuccessfully(){
        Long id = 1L;
        User user = new User("User1", "email1@email.com", "address1", Role.USER, "password1", false);
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        assertThrows(UserNotActiveException.class, () -> userValidationService.validateUser(id));
    }
}