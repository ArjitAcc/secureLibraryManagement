package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.UserRequestDto;
import com.assignment6.secureLibraryManagement.dto.UserResponseDto;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long addBook(UserRequestDto userRequestDto){
        User user = new User(userRequestDto.getName(), userRequestDto.getEmailAddress(), userRequestDto.getAddress(), userRequestDto.getRole());
        String userRole = String.valueOf(user.getRole());
        // implement role check
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    public void updateUser(UserRequestDto userRequestDto, Long userId){
        boolean isExist = userRepository.existsById(userId);
        if(!isExist) return;
        User user = new User(userRequestDto.getName(), userRequestDto.getEmailAddress(), userRequestDto.getAddress(), userRequestDto.getRole());
        userRepository.save(user);
    }
    public UserResponseDto getUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(null);
        if(user == null) return null;
        UserResponseDto userReturned = new UserResponseDto(user.getName(), user.getEmailAddress(), user.getAddress(), user.getRole());
        return userReturned;
    }
    public void removeUser(Long userId){
        userRepository.deleteById(userId);
    }
}
