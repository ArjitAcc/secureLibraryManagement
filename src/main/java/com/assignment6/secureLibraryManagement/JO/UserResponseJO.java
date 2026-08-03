package com.assignment6.secureLibraryManagement.JO;

import com.assignment6.secureLibraryManagement.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserResponseJO(
    String name,
    String emailAddress,
    String address,
    @Enumerated(EnumType.STRING)
    Role role,
    boolean isActive
) {
}
