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
import com.assignment6.secureLibraryManagement.service.BookService;
import com.assignment6.secureLibraryManagement.service.BorrowRecordService;
import com.assignment6.secureLibraryManagement.service.UserValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserValidationService userValidationService;
    private final BookService bookService;

    private void updateBookCopy(Book book, int copy){
        book.setAvailableCopies(copy);
        bookRepository.save(book);
    }

    private User getUserByEmailAddress(String emailAddress){
        return userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() ->  new UserNotFoundException("User not found!"));
    }

    private void validateBookAvailability(Book book){
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Book not available");
        }
    }

    private void validateUserBorrowStatus(User user, Book book, BorrowStatus borrowStatus){
        if (borrowRecordRepository.existsByUserAndBookAndStatus(user, book, borrowStatus)) {
            throw new AlreadyBorrowedException("Already borrowed this book");
        }
    }

    private void setBorrowStatus(BorrowRecord record, BorrowStatus borrowStatus){
        record.setStatus(borrowStatus);
        if (borrowStatus == BorrowStatus.BORROWED) {
            record.setBorrowDate(LocalDateTime.now());
        } else {
            record.setReturnDate(LocalDateTime.now());
        }
    }

    private BorrowRecord buildBorrowRecord(User user, Book book, BorrowStatus borrowStatus){
        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        setBorrowStatus(record, borrowStatus);
        return record;
    }

    public Long borrowBook(String emailAddress, Long bookId) {

        User user = getUserByEmailAddress(emailAddress);

        userValidationService.validateUser(user.getId());

        Book book = bookService.getBook(bookId);
        validateBookAvailability(book);
        validateUserBorrowStatus(user, book, BorrowStatus.BORROWED);

        BorrowRecord record = buildBorrowRecord(user, book, BorrowStatus.BORROWED);
        borrowRecordRepository.save(record);

        updateBookCopy(book, book.getAvailableCopies() - 1);

        return record.getId();
    }

    public void returnBook(Long borrowRecordId){
        BorrowRecord borrowRecord = getRecord(borrowRecordId);
        setBorrowStatus(borrowRecord, BorrowStatus.RETURNED);
        Book book = borrowRecord.getBook();
        updateBookCopy(book, book.getAvailableCopies() - 1);

        borrowRecordRepository.save(borrowRecord);
    }

    public BorrowRecord getRecord(Long borrowRecordId){
        return borrowRecordRepository.findById(borrowRecordId).orElseThrow(() -> new BorrowRecordNotFoundException("Record not found!"));
    }

    public List<BorrowRecord> getBookRecords(String emailAddress){
        return borrowRecordRepository.findByUserEmailAddress(emailAddress);
    }

}

