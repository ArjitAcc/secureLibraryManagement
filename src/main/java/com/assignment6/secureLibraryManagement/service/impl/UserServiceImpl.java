package com.assignment6.secureLibraryManagement.service.impl;

import com.assignment6.secureLibraryManagement.jo.UserRequestJO;
import com.assignment6.secureLibraryManagement.jo.UserResponseJO;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UnauthorizedRequestException;
import com.assignment6.secureLibraryManagement.exception.UserNotFoundException;
import com.assignment6.secureLibraryManagement.mapper.UserJOMapper;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import com.assignment6.secureLibraryManagement.service.AuthorizeService;
import com.assignment6.secureLibraryManagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorizeService authorizeService;

    public Long addUser(User user){
        userRepository.save(user);
        return user.getId();
    }

    public void updateUser(User updateUser, Long userId, Authentication authentication){
        getUser(userId, authentication);
        updateUser.setId(userId);
        userRepository.save(updateUser);
    }

    public User getUser(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorizeService.authorize(user.getEmailAddress(), authentication);
        return user;
    }

    public void deleteUser(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorizeService.authorize(user.getEmailAddress(), authentication);
        user.setActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorizeService.authorize(user.getEmailAddress(), authentication);
        user.setActive(true);
        userRepository.save(user);
    }
}
