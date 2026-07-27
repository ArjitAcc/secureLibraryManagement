package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String emailAddress;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String name, String emailAddress, String address, Role role){
        this.name = name;
        this.emailAddress = emailAddress;
        this.address = address;
        this.role = role;
    }
}
