package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import jakarta.transaction.Transactional;

import java.util.List;

public interface BorrowRecordService {

    @Transactional
    Long borrowBook(String emailAddress, Long bookId);

    void returnBook(Long borrowRecordId);

    BorrowRecord getBorrowedBookRecord(Long borrowRecordId);

    List<BorrowRecord> getBorrowedBookRecords(String emailAddress);
}

