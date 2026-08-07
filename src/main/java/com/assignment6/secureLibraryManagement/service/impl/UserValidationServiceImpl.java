package com.assignment6.secureLibraryManagement.service.impl;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UserNotActiveException;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import com.assignment6.secureLibraryManagement.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {
    private final UserRepository userRepository;

    public void validateUser(Long userId){
        userRepository.findById(userId).filter(User::isActive).orElseThrow(()-> new UserNotActiveException("User not active"));
    }
}
