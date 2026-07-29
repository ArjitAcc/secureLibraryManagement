package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import com.assignment6.secureLibraryManagement.entity.BorrowStatus;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.repository.BorrowRecordRepository;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Transactional
    public BorrowRecord borrowBook(String emailAddress, Long bookId) {

        User user = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book not available");
        }

        if (borrowRecordRepository.existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED)) {
            throw new RuntimeException("Already borrowed this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setStatus(BorrowStatus.BORROWED);

        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long borrowRecordId){
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        return borrowRecord;
    }

    public List<BorrowRecord> getBookRecords(String emailAddress){
        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUserEmailAddress(emailAddress);
        return borrowRecords;
    }
}

