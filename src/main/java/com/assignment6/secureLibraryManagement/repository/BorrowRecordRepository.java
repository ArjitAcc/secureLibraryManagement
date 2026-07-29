package com.assignment6.secureLibraryManagement.repository;

import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import com.assignment6.secureLibraryManagement.entity.BorrowStatus;
import com.assignment6.secureLibraryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserEmailAddress(String emailAddress);

    boolean existsByUserAndBookAndStatus(User user, Book book, BorrowStatus status);
}