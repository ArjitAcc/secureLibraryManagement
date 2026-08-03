package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.JO.UserRequestJO;
import com.assignment6.secureLibraryManagement.JO.UserResponseJO;
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
        Long userSavedId = userService.addUser(userRequestJO);
        return ResponseEntity.ok(userSavedId);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponseJO> getUser(@PathVariable("user-id") Long userId, Authentication authentication){
        UserResponseJO userReturned = userService.getUser(userId, authentication);
        if(userReturned == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userReturned);
    }

    @PutMapping("/{user-id}")
    public void updateUser(@Valid @RequestBody UserRequestJO userRequestJO, @PathVariable("user-id") Long userId, Authentication authentication){
        userService.updateUser(userRequestJO, userId, authentication);
    }

    @DeleteMapping("/{user-id}")
    public void removeUser(@PathVariable("user-id") Long userId, Authentication authentication){
        userService.softDelete(userId, authentication);
    }

    @PatchMapping("/{user-id}")
    public void activateUser(@PathVariable("user-id") Long userId, Authentication authentication){
        userService.setActive(userId, authentication);
    }
}
