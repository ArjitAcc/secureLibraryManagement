package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.UserRequestJO;
import com.assignment6.secureLibraryManagement.dto.UserResponseJO;
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

    public Long addUser(UserRequestJO userRequestJO){
        User user = new User(userRequestJO.name(), userRequestJO.emailAddress(), userRequestJO.address(), userRequestJO.role(), passwordEncoder.encode(userRequestJO.password()), userRequestJO.isActive());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    public void updateUser(UserRequestJO userRequestJO, Long userId, Authentication authentication){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isEmpty()) return;
        boolean isAuthorised = isAuthorised(userRequestJO.emailAddress(), authentication);
        if(!isAuthorised) return;
        user.ifPresent(u -> {
            u.setRole(userRequestJO.role());
            u.setName(userRequestJO.name());
            u.setAddress(userRequestJO.address());
            u.setEmailAddress(userRequestJO.emailAddress());
            u.setPassword(passwordEncoder.encode(userRequestJO.password()));
            u.setActive(userRequestJO.isActive());
            userRepository.save(u);
        });
    }
    public UserResponseJO getUser(Long userId, Authentication authentication){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return null;
        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
        if(!isAuthorised) return null;
        UserResponseJO userReturned = new UserResponseJO(user.getName(), user.getEmailAddress(), user.getAddress(), user.getRole(), user.isActive());
        return userReturned;
    }
    public void softDelete(Long userId, Authentication authentication){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return;
        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
        if(!isAuthorised) return;
        user.setActive(false);
        userRepository.save(user);
//        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
//        if(!isAuthorised) return;
//        userRepository.deleteById(userId);
    }
    public void setActive(Long userId, Authentication authentication){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return;
        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
        if(!isAuthorised) return;
        user.setActive(true);
        userRepository.save(user);
    }
}
