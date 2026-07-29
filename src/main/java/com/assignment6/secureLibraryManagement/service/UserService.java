package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.UserRequestDto;
import com.assignment6.secureLibraryManagement.dto.UserResponseDto;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // store encrypted password

    private boolean isAuthorised(String emailAddress, Authentication authentication){
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if(role.equals("ADMIN")) return true;
        return authentication.getName().equals(emailAddress);
    }

    public Long addBook(UserRequestDto userRequestDto){
        User user = new User(userRequestDto.getName(), userRequestDto.getEmailAddress(), userRequestDto.getAddress(), userRequestDto.getRole(), passwordEncoder.encode(userRequestDto.getPassword()));
        String userRole = String.valueOf(user.getRole());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    public void updateUser(UserRequestDto userRequestDto, Long userId, Authentication authentication){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isEmpty()) return;
        boolean isAuthorised = isAuthorised(userRequestDto.getEmailAddress(), authentication);
        if(!isAuthorised) return;
        user.ifPresent(u -> {
            u.setRole(userRequestDto.getRole());
            u.setName(userRequestDto.getName());
            u.setAddress(userRequestDto.getAddress());
            u.setEmailAddress(userRequestDto.getEmailAddress());
            u.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            userRepository.save(u);
        });
    }
    public UserResponseDto getUser(Long userId, Authentication authentication){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return null;
        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
        if(!isAuthorised) return null;
        UserResponseDto userReturned = new UserResponseDto(user.getName(), user.getEmailAddress(), user.getAddress(), user.getRole());
        return userReturned;
    }
    public void removeUser(Long userId, Authentication authentication){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return;
        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
        if(!isAuthorised) return;
        userRepository.deleteById(userId);
    }
}
