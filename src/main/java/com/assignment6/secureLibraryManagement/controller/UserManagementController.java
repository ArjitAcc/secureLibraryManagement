package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.UserRequestJO;
import com.assignment6.secureLibraryManagement.dto.UserResponseJO;
import com.assignment6.secureLibraryManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userManangement")
public class UserManagementController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<Long> addUser(@Valid  @RequestBody UserRequestJO userRequestJO){
        Long userSavedId = userService.addBook(userRequestJO);
        return ResponseEntity.ok(userSavedId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseJO> getUser(@PathVariable Long userId, Authentication authentication){
        UserResponseJO userReturned = userService.getUser(userId, authentication);
        if(userReturned == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userReturned);
    }

    @PutMapping("/{userId}")
    public void updateUser(@Valid @RequestBody UserRequestJO userRequestJO, @PathVariable Long userId, Authentication authentication){
        userService.updateUser(userRequestJO, userId, authentication);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable Long userId, Authentication authentication){
        userService.removeUser(userId, authentication);
    }
}
