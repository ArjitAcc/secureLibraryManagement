package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.UserRequestJO;
import com.assignment6.secureLibraryManagement.jo.UserResponseJO;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UnauthorizedRequestException;
import com.assignment6.secureLibraryManagement.exception.UserNotFoundException;
import com.assignment6.secureLibraryManagement.mapper.UserJOMapper;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public interface UserService {

    Long addUser(User user);

    void updateUser(User updatedUser, Long userId, Authentication authentication);

    User getUser(Long userId, Authentication authentication);

    void deleteUser(Long userId, Authentication authentication);

    void activateUser(Long userId, Authentication authentication);

}
