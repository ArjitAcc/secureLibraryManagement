package com.assignment6.secureLibraryManagement.mapper;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.jo.UserRequestJO;
import com.assignment6.secureLibraryManagement.jo.UserResponseJO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserJOMapper {
    public void mapFromJO(UserRequestJO userRequestJO, User user, PasswordEncoder passwordEncoder){
        user.setRole(userRequestJO.role());
        user.setName(userRequestJO.name());
        user.setAddress(userRequestJO.address());
        user.setEmailAddress(userRequestJO.emailAddress());
        user.setPassword(passwordEncoder.encode(userRequestJO.password()));
        user.setActive(userRequestJO.isActive());
    }
    public UserResponseJO mapToPOJO(User user){
        return new UserResponseJO(user.getName(), user.getEmailAddress(), user.getAddress(), user.getRole(), user.isActive());
    }
}
