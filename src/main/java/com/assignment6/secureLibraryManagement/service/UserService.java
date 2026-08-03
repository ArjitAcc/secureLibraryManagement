package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.JO.UserRequestJO;
import com.assignment6.secureLibraryManagement.JO.UserResponseJO;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.UnauthorizedRequestException;
import com.assignment6.secureLibraryManagement.exception.UserNotFoundException;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // store encrypted password

    private void authorize(String emailAddress, Authentication authentication){
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        assert role != null;
        if(role.equals("ADMIN")) return;
        if(!authentication.getName().equals(emailAddress)) {
            throw new UnauthorizedRequestException("user does not have access to others data");
        }
    }

    public Long addUser(UserRequestJO userRequestJO){
        User user = new User(userRequestJO.name(), userRequestJO.emailAddress(), userRequestJO.address(), userRequestJO.role(), passwordEncoder.encode(userRequestJO.password()), userRequestJO.isActive());
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    public void updateUser(UserRequestJO userRequestJO, Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorize(user.getEmailAddress(), authentication);
        user.setRole(userRequestJO.role());
        user.setName(userRequestJO.name());
        user.setAddress(userRequestJO.address());
        user.setEmailAddress(userRequestJO.emailAddress());
        user.setPassword(passwordEncoder.encode(userRequestJO.password()));
        user.setActive(userRequestJO.isActive());
        userRepository.save(user);
    }
    public UserResponseJO getUser(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorize(user.getEmailAddress(), authentication);
        return new UserResponseJO(user.getName(), user.getEmailAddress(), user.getAddress(), user.getRole(), user.isActive());
    }
    public void softDelete(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorize(user.getEmailAddress(), authentication);
        user.setActive(false);
        userRepository.save(user);
//        boolean isAuthorised = isAuthorised(user.getEmailAddress(), authentication);
//        if(!isAuthorised) return;
//        userRepository.deleteById(userId);
    }
    public void setActive(Long userId, Authentication authentication){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found!"));
        authorize(user.getEmailAddress(), authentication);
        user.setActive(true);
        userRepository.save(user);
    }
}
