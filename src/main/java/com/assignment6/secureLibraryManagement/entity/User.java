package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
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

    private String name;

    @Column(unique = true)
    private String emailAddress;

    private String address;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive = false;

    public User(String name, String emailAddress, String address, Role role, String password, boolean isActive){
        this.name = name;
        this.emailAddress = emailAddress;
        this.address = address;
        this.role = role;
        this.password = password;
        this.isActive = isActive;
    }
}
