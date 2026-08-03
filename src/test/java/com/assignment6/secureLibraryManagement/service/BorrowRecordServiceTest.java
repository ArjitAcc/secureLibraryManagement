package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BorrowRecordResponseJO;
import com.assignment6.secureLibraryManagement.entity.*;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.repository.BorrowRecordRepository;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceTest {
    @Mock
    BorrowRecordRepository borrowRecordRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    UserValidationService userValidationService;
    @InjectMocks
    BorrowRecordService borrowRecordService;

    User user;
    Book book;

    @BeforeEach
    void init(){
        user = new User("name", "email@domain.com", "address", Role.USER, "password", true);
        user.setId(1L);
        book = new Book();
        book.setId(1001L);
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setIsbn("Book ISBN");
        book.setPrice(450);
        book.setAvailableCopies(2);
    }

    @Test
    void borrowBookShouldBorrowBookSuccessfully() {
        String userEmail = user.getEmailAddress();
        Long bookId = book.getId();
        Mockito.when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.of(user));
        doNothing().when(userValidationService).validateUser(user.getId());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED)).thenReturn(false);
        BorrowRecord record = new BorrowRecord();
        record.setId(101L);
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setStatus(BorrowStatus.BORROWED);

        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(record);
        when(bookRepository.save(book)).thenReturn(book);
        BorrowRecordResponseJO recordResponseJO = borrowRecordService.borrowBook(userEmail, bookId);
        assertEquals(record.getId(), recordResponseJO.id());
        assertEquals(user, recordResponseJO.user());
        assertEquals(book, recordResponseJO.book());
        assertEquals(BorrowStatus.BORROWED, recordResponseJO.status());
        assertNotNull(recordResponseJO.borrowDate());
        assertNull(recordResponseJO.returnDate());
        assertEquals(1, book.getAvailableCopies());
        verify(userValidationService).validateUser(user.getId());
        verify(bookRepository).save(book);
    }

    @Test
    void returnBookShouldReturnBookSuccessfully() {
        BorrowRecord record = new BorrowRecord();
        record.setId(101L);
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setStatus(BorrowStatus.BORROWED);

        when(borrowRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));
        Book bookReturned = record.getBook();
        when(bookRepository.save(bookReturned)).thenReturn(bookReturned);
        record.setStatus(BorrowStatus.RETURNED);
        when(borrowRecordRepository.save(record)).thenReturn(record);
        BorrowRecordResponseJO recordResponseJO = borrowRecordService.returnBook(record.getId());
        assertEquals(record.getId(), recordResponseJO.id());
        assertEquals(user, recordResponseJO.user());
        assertEquals(book, recordResponseJO.book());
        assertEquals(BorrowStatus.RETURNED, recordResponseJO.status());
        assertNotNull(recordResponseJO.borrowDate());
        assertNotNull(recordResponseJO.returnDate());
        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    void getBookRecordsShouldGetBookRecordsSuccessfully() {
        BorrowRecord record1 = new BorrowRecord();
        record1.setId(101L);
        record1.setUser(user);
        record1.setBook(book);
        record1.setBorrowDate(LocalDateTime.now());
        record1.setStatus(BorrowStatus.BORROWED);

        Book book1 = new Book();
        book1.setId(1002L);
        book1.setTitle("Book Title1");
        book1.setAuthor("Book Author1");
        book1.setIsbn("Book ISBN1");
        book1.setPrice(250);
        book1.setAvailableCopies(1);

        BorrowRecord record2 = new BorrowRecord();
        record2.setId(101L);
        record2.setUser(user);
        record2.setBook(book1);
        record2.setBorrowDate(LocalDateTime.now());
        record2.setStatus(BorrowStatus.BORROWED);

        List<BorrowRecord> records = List.of(record1, record2);
        when(borrowRecordRepository.findByUserEmailAddress(user.getEmailAddress())).thenReturn(records);
        List<BorrowRecordResponseJO> recordResponseJOList = borrowRecordService.getBookRecords(user.getEmailAddress());
        assertEquals(records.size(), recordResponseJOList.size());
        for(int i = 0; i < records.size(); i++){
            assertEquals(records.get(i).getId(), recordResponseJOList.get(i).id());
            assertEquals(records.get(i).getUser(), recordResponseJOList.get(i).user());
            assertEquals(records.get(i).getBook(), recordResponseJOList.get(i).book());
            assertEquals(records.get(i).getStatus(), recordResponseJOList.get(i).status());
            assertNotNull(recordResponseJOList.get(i).borrowDate());
            assertNull(recordResponseJOList.get(i).returnDate());
        }
    }
}