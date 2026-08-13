package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.BorrowRecordResponseJO;
import com.assignment6.secureLibraryManagement.entity.*;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.repository.BorrowRecordRepository;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import com.assignment6.secureLibraryManagement.service.impl.BorrowRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    String MOCK_USER_EMAIL = "email@domain.com";
    Long MOCK_USER_ID = 1L;
    Long MOCK_BOOK_ID = 1001L;
    Long MOCK_BORROW_RECORD_ID = 101L;

    @Mock
    BorrowRecordRepository borrowRecordRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    UserValidationService userValidationService;
    @Mock
    BookService bookService;
    @InjectMocks
    BorrowRecordServiceImpl borrowRecordService;

    @Test
    void borrowBookShouldBorrowBookSuccessfully() {
        String userEmail = MOCK_USER_EMAIL;
        Long bookId = MOCK_BOOK_ID;
        User user = buildUser();
        Book book = buildBook();
        when(userRepository.findByEmailAddress(isA(String.class))).thenReturn(Optional.of(user));
        when(bookService.getBook(anyLong())).thenReturn(book);
        when(borrowRecordRepository.existsByUserAndBookAndStatus(isA(User.class), any(Book.class), any(BorrowStatus.class))).thenReturn(false);
        when(borrowRecordRepository.save(isA(BorrowRecord.class))).thenAnswer(invocation -> {
            BorrowRecord record = invocation.getArgument(0);
            record.setId(MOCK_BORROW_RECORD_ID);
            return record;
        });

        Long recordId = borrowRecordService.borrowBook(userEmail, bookId);

        assertEquals(recordId, MOCK_BORROW_RECORD_ID);
        assertEquals(1, book.getAvailableCopies());
        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verify(bookService).getBook(MOCK_BOOK_ID);
        verify(borrowRecordRepository).existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED);
        ArgumentCaptor<BorrowRecord> recordCaptor = ArgumentCaptor.forClass(BorrowRecord.class);
        verify(borrowRecordRepository).save(recordCaptor.capture());
        BorrowRecord record = recordCaptor.getValue();
        assertEquals(user, record.getUser());
        assertEquals(book, record.getBook());
        assertEquals(BorrowStatus.BORROWED, record.getStatus());
        assertNotNull(record.getBorrowDate());
        assertEquals(101L, record.getId());
        verify(bookRepository).save(book);
    }



//    @Test
//    void returnBookShouldReturnBookSuccessfully() {
//        BorrowRecord record = new BorrowRecord();
//        record.setId(101L);
//        record.setUser(user);
//        record.setBook(book);
//        record.setBorrowDate(LocalDateTime.now());
//        record.setStatus(BorrowStatus.BORROWED);
//
//        when(borrowRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));
//        Book bookReturned = record.getBook();
//        when(bookRepository.save(bookReturned)).thenReturn(bookReturned);
//        record.setStatus(BorrowStatus.RETURNED);
//        when(borrowRecordRepository.save(record)).thenReturn(record);
//        BorrowRecordResponseJO recordResponseJO = borrowRecordService.returnBook(record.getId());
//        assertEquals(record.getId(), recordResponseJO.id());
//        assertEquals(user, recordResponseJO.user());
//        assertEquals(book, recordResponseJO.book());
//        assertEquals(BorrowStatus.RETURNED, recordResponseJO.status());
//        assertNotNull(recordResponseJO.borrowDate());
//        assertNotNull(recordResponseJO.returnDate());
//        assertEquals(3, book.getAvailableCopies());
//    }
//
//    @Test
//    void getBookRecordsShouldGetBookRecordsSuccessfully() {
//        BorrowRecord record1 = new BorrowRecord();
//        record1.setId(101L);
//        record1.setUser(user);
//        record1.setBook(book);
//        record1.setBorrowDate(LocalDateTime.now());
//        record1.setStatus(BorrowStatus.BORROWED);
//
//        Book book1 = new Book();
//        book1.setId(1002L);
//        book1.setTitle("Book Title1");
//        book1.setAuthor("Book Author1");
//        book1.setIsbn("Book ISBN1");
//        book1.setPrice(250);
//        book1.setAvailableCopies(1);
//
//        BorrowRecord record2 = new BorrowRecord();
//        record2.setId(101L);
//        record2.setUser(user);
//        record2.setBook(book1);
//        record2.setBorrowDate(LocalDateTime.now());
//        record2.setStatus(BorrowStatus.BORROWED);
//
//        List<BorrowRecord> records = List.of(record1, record2);
//        when(borrowRecordRepository.findByUserEmailAddress(user.getEmailAddress())).thenReturn(records);
//        List<BorrowRecordResponseJO> recordResponseJOList = borrowRecordService.getBookRecords(user.getEmailAddress());
//        assertEquals(records.size(), recordResponseJOList.size());
//        for(int i = 0; i < records.size(); i++){
//            assertEquals(records.get(i).getId(), recordResponseJOList.get(i).id());
//            assertEquals(records.get(i).getUser(), recordResponseJOList.get(i).user());
//            assertEquals(records.get(i).getBook(), recordResponseJOList.get(i).book());
//            assertEquals(records.get(i).getStatus(), recordResponseJOList.get(i).status());
//            assertNotNull(recordResponseJOList.get(i).borrowDate());
//            assertNull(recordResponseJOList.get(i).returnDate());
//        }
//    }

    private User buildUser(){
        User user = new User("name", MOCK_USER_EMAIL, "address", Role.USER, "password", true);
        user.setId(MOCK_USER_ID);
        return user;
    }

    private Book buildBook(){
        Book book = new Book("Book Title", "Book Author", "Book ISBN", 450, 2);
        book.setId(MOCK_BOOK_ID);
        return book;
    }

    private BorrowRecord buildBorrowRecord(User user, Book book, Long id){
        BorrowRecord record = new BorrowRecord(user, book, LocalDateTime.now(), null, BorrowStatus.BORROWED);
        record.setId(id);
        return record;
    }
}