package com.assignment6.secureLibraryManagement.repository;

import com.assignment6.secureLibraryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(String emailAddress);
    Optional<User> findByName(String name);
}