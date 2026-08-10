package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    public BorrowRecord(User user, Book book, LocalDateTime borrowDate, LocalDateTime returnDate, BorrowStatus status) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}