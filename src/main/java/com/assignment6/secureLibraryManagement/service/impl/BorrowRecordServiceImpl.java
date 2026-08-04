package com.assignment6.secureLibraryManagement.service.impl;

import com.assignment6.secureLibraryManagement.jo.BorrowRecordResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import com.assignment6.secureLibraryManagement.entity.BorrowStatus;
import com.assignment6.secureLibraryManagement.entity.User;
import com.assignment6.secureLibraryManagement.exception.*;
import com.assignment6.secureLibraryManagement.mapper.BorrowRecordJOMapper;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.repository.BorrowRecordRepository;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import com.assignment6.secureLibraryManagement.service.UserValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserValidationService userValidationService;
    private final BorrowRecordJOMapper borrowRecordJOMapper;

    @Transactional
    public BorrowRecordResponseJO borrowBook(String emailAddress, Long bookId) {

        User user = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() ->  new UserNotFoundException("User not found!"));

        userValidationService.validateUser(user.getId());

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Book not available");
        }

        if (borrowRecordRepository.existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED)) {
            throw new AlreadyBorrowedException("Already borrowed this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setStatus(BorrowStatus.BORROWED);

        BorrowRecord recordSaved = borrowRecordRepository.save(record);
        bookRepository.save(book);
        return borrowRecordJOMapper.mapToPOJO(recordSaved);
    }

    public BorrowRecordResponseJO returnBook(Long borrowRecordId){
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(borrowRecord);
        return borrowRecordJOMapper.mapToPOJO(borrowRecord);
    }

    public List<BorrowRecordResponseJO> getBookRecords(String emailAddress){
        return borrowRecordRepository.findByUserEmailAddress(emailAddress).stream().map(borrowRecordJOMapper::mapToPOJO).toList();
    }
}

