package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UserNotActiveException;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserRepository userRepository;

    void validateUser(Long userId){
        userRepository.findById(userId).filter(User::isActive).orElseThrow(()-> new UserNotActiveException("User not active"));
    }
}
