package com.assignment6.secureLibraryManagement.JO;

import com.assignment6.secureLibraryManagement.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestJO(
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 3, max = 50)
    String name,

    @NotBlank
    @Email
    String emailAddress,

    @NotBlank
    String address,

    @NotBlank
    String password,

    @Enumerated(EnumType.STRING)
    Role role,

    boolean isActive
) {
}
