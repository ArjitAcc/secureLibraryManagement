package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BorrowRecordResponseJO;
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
    public BorrowRecordResponseJO borrowBook(String emailAddress, Long bookId) {

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

        BorrowRecord recordSaved = borrowRecordRepository.save(record);
        bookRepository.save(book);
        return new BorrowRecordResponseJO(recordSaved.getId(), recordSaved.getUser(), recordSaved.getBook(), recordSaved.getBorrowDate(), recordSaved.getReturnDate(), recordSaved.getStatus());
    }

    public BorrowRecordResponseJO returnBook(Long borrowRecordId){
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        return new BorrowRecordResponseJO(borrowRecord.getId(), borrowRecord.getUser(), borrowRecord.getBook(), borrowRecord.getBorrowDate(), borrowRecord.getReturnDate(), borrowRecord.getStatus());
    }

    public List<BorrowRecordResponseJO> getBookRecords(String emailAddress){
        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUserEmailAddress(emailAddress);
        List<BorrowRecordResponseJO> responseRecords = borrowRecords.stream().map((r) -> {
            return new BorrowRecordResponseJO(r.getId(), r.getUser(), r.getBook(), r.getBorrowDate(), r.getReturnDate(), r.getStatus());
        }).toList();
        return responseRecords;
    }
}

