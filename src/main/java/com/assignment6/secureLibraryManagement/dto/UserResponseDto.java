package com.assignment6.secureLibraryManagement.dto;

import com.assignment6.secureLibraryManagement.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String name;
    private String emailAddress;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
}
