package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress)
            throws UsernameNotFoundException {

        User user = repository.findByEmailAddress(emailAddress)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailAddress())   // Spring calls it username
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
