package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 3, max = 50)
    private String name;

    @Column(unique = true)
    @NotBlank
    @Email
    private String emailAddress;

    @NotBlank
    private String address;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String name, String emailAddress, String address, Role role, String password){
        this.name = name;
        this.emailAddress = emailAddress;
        this.address = address;
        this.role = role;
        this.password = password;
    }
}
