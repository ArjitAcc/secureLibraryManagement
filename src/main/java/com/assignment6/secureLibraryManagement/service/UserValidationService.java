package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UserNotActiveException;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface UserValidationService {
    void validateUser(Long userId);
}
