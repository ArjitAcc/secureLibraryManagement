package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.exception.UnauthorizedRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

public interface AuthorizeService {
    void authorize(String emailAddress, Authentication authentication);
}
