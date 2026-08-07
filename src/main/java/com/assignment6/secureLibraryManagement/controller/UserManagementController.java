package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.jo.UserRequestJO;
import com.assignment6.secureLibraryManagement.jo.UserResponseJO;
import com.assignment6.secureLibraryManagement.mapper.UserJOMapper;
import com.assignment6.secureLibraryManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userManangement")
public class UserManagementController {
    private final UserService userService;
    private final UserJOMapper userJOMapper;
    private final PasswordEncoder passwordEncoder;

    private User buildUser(UserRequestJO userRequestJO){
        User user = new User();
        userJOMapper.mapFromJO(userRequestJO, user, passwordEncoder);
        return user;
    }

    @PostMapping("/")
    public ResponseEntity<Long> addUser(@Valid  @RequestBody UserRequestJO userRequestJO){
        Long userSavedId = userService.addUser(buildUser(userRequestJO));
        return ResponseEntity.ok(userSavedId);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponseJO> getUser(@PathVariable("user-id") Long userId, Authentication authentication){
        UserResponseJO userReturned = userService.getUser(userId, authentication);
        return ResponseEntity.ok(userReturned);
    }

    @PutMapping("/{user-id}")
    public void updateUser(@Valid @RequestBody UserRequestJO userRequestJO, @PathVariable("user-id") Long userId, Authentication authentication){
        userService.updateUser(userRequestJO, userId, authentication);
    }

    @DeleteMapping("/{user-id}")
    public void removeUser(@PathVariable("user-id") Long userId, Authentication authentication){
        userService.deleteUser(userId, authentication);
    }

    @PatchMapping("/{user-id}")
    public void activateUser(@PathVariable("user-id") Long userId, Authentication authentication){
        userService.setActive(userId, authentication);
    }
}
