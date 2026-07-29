package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.UserRequestDto;
import com.assignment6.secureLibraryManagement.dto.UserResponseDto;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userManangement")
public class UserManagementController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<Long> addUser(@RequestBody UserRequestDto userRequestDto){
        Long userSavedId = userService.addBook(userRequestDto);
        return ResponseEntity.ok(userSavedId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId, Authentication authentication){
        UserResponseDto userReturned = userService.getUser(userId, authentication);
        if(userReturned == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userReturned);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRequestDto userRequestDto, @PathVariable Long userId, Authentication authentication){
        userService.updateUser(userRequestDto, userId, authentication);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable Long userId, Authentication authentication){
        userService.removeUser(userId, authentication);
    }
}
