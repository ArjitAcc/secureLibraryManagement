package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.UserRequestDto;
import com.assignment6.secureLibraryManagement.dto.UserResponseDto;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId){
        UserResponseDto userReturned = userService.getUser(userId);
        return ResponseEntity.ok(userReturned);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRequestDto userRequestDto, @PathVariable Long userId){
        userService.updateUser(userRequestDto, userId);
        return;
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable Long userId){
        userService.removeUser(userId);
    }
}
