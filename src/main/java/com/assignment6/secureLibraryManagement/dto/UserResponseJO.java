package com.assignment6.secureLibraryManagement.dto;

import com.assignment6.secureLibraryManagement.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

public record UserResponseJO(
    String name,
    String emailAddress,
    String address,
    @Enumerated(EnumType.STRING)
    Role role,
    boolean isActive
) {
}
