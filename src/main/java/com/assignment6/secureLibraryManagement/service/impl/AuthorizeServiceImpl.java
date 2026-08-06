package com.assignment6.secureLibraryManagement.service.impl;

import com.assignment6.secureLibraryManagement.exception.UnauthorizedRequestException;
import com.assignment6.secureLibraryManagement.service.AuthorizeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    public void authorize(String emailAddress, Authentication authentication){
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        assert role != null;
        if(role.equals("ADMIN")) return;
        if(!authentication.getName().equals(emailAddress)) {
            throw new UnauthorizedRequestException("user does not have access to others data");
        }
    }
}

