package com.assignment6.secureLibraryManagement.repository;

import com.assignment6.secureLibraryManagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}