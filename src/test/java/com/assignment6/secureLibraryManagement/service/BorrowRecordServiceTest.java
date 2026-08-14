package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.entity.*;
import com.assignment6.secureLibraryManagement.exception.*;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.repository.BorrowRecordRepository;
import com.assignment6.secureLibraryManagement.repository.UserRepository;
import com.assignment6.secureLibraryManagement.service.impl.BorrowRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceTest {
    String MOCK_USER_EMAIL = "email@domain.com";
    long MOCK_USER_ID = 1L;
    Long MOCK_BOOK_ID = 1001L;
    Long MOCK_BORROW_RECORD_ID = 101L;
    Long MOCK_OTHER_BORROW_RECORD_ID = 101L;
    int MOCK_BOOK_AVAILABLE_COPIES = 2;

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
        User user = buildUser();
        Book book = buildBook(MOCK_BOOK_AVAILABLE_COPIES);
        when(userRepository.findByEmailAddress(isA(String.class))).thenReturn(Optional.of(user));
        when(bookService.getBook(anyLong())).thenReturn(book);
        when(borrowRecordRepository.existsByUserAndBookAndStatus(isA(User.class), isA(Book.class), isA(BorrowStatus.class))).thenReturn(false);
        when(borrowRecordRepository.save(isA(BorrowRecord.class))).thenAnswer(invocation -> {
            BorrowRecord record = invocation.getArgument(0);
            record.setId(MOCK_BORROW_RECORD_ID);
            return record;
        });

        assertThat(borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID)).isEqualTo(MOCK_BORROW_RECORD_ID);
        assertEquals(1, book.getAvailableCopies());

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verify(bookService).getBook(MOCK_BOOK_ID);
        verify(borrowRecordRepository).existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED);
        verify(borrowRecordRepository).save(refEq(buildBorrowRecord(user, book), "borrowDate"));
        verify(bookRepository).save(ArgumentMatchers.same(book));
    }

    @Test
    void borrowBookShouldThrowErrorWhenUserNotFound() {
        when(userRepository.findByEmailAddress(MOCK_USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID));

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verifyNoInteractions(userValidationService, bookService, borrowRecordRepository, bookRepository);
    }

    @Test
    void borrowBookShouldThrowErrorWhenUserNotActive() {
        User user = buildUser();
        when(userRepository.findByEmailAddress(MOCK_USER_EMAIL)).thenReturn(Optional.of(user));
        doThrow(new UserNotActiveException("User not active")).when(userValidationService).validateUser(MOCK_USER_ID);

        assertThrows(UserNotActiveException.class, () -> borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID));

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verifyNoInteractions(bookService, borrowRecordRepository, bookRepository);
    }

    @Test
    void borrowBookShouldThrowErrorWhenBookNotFound() {
        User user = buildUser();
        when(userRepository.findByEmailAddress(MOCK_USER_EMAIL)).thenReturn(Optional.of(user));
        doThrow(new BookNotFoundException("Book not found")).when(bookService).getBook(MOCK_BOOK_ID);

        assertThrows(BookNotFoundException.class, () -> borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID));

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verify(bookService).getBook(MOCK_BOOK_ID);
        verifyNoInteractions(borrowRecordRepository, bookRepository);
    }

    @Test
    void borrowBookShouldThrowErrorWhenBookNotAvailable() {
        User user = buildUser();
        Book book = buildBook(0);
        when(userRepository.findByEmailAddress(MOCK_USER_EMAIL)).thenReturn(Optional.of(user));
        when(bookService.getBook(MOCK_BOOK_ID)).thenReturn(book);

        assertThrows(BookNotAvailableException.class, () -> borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID));

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verify(bookService).getBook(MOCK_BOOK_ID);
        verifyNoInteractions(borrowRecordRepository, bookRepository);
    }

    @Test
    void borrowBookShouldThrowErrorWhenBookAlreadyBorrowed() {
        User user = buildUser();
        Book book = buildBook(MOCK_BOOK_AVAILABLE_COPIES);
        when(userRepository.findByEmailAddress(MOCK_USER_EMAIL)).thenReturn(Optional.of(user));
        when(bookService.getBook(MOCK_BOOK_ID)).thenReturn(book);
        when(borrowRecordRepository.existsByUserAndBookAndStatus(isA(User.class), isA(Book.class), isA(BorrowStatus.class))).thenReturn(true);

        assertThrows(AlreadyBorrowedException.class, () -> borrowRecordService.borrowBook(MOCK_USER_EMAIL, MOCK_BOOK_ID));

        verify(userRepository).findByEmailAddress(MOCK_USER_EMAIL);
        verify(userValidationService).validateUser(MOCK_USER_ID);
        verify(bookService).getBook(MOCK_BOOK_ID);
        verify(borrowRecordRepository).existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED);
        verify(borrowRecordRepository, never()).save(isA(BorrowRecord.class));
        verify(bookRepository, never()).save(isA(Book.class));
    }

    @Test
    void getRecordShouldGetRecordSuccessfully(){
        User user = buildUser();
        Book book = buildBook(MOCK_BOOK_AVAILABLE_COPIES);
        BorrowRecord record = buildBorrowRecord(user, book);
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.of(record));

        assertThat(borrowRecordService.getRecord(MOCK_BORROW_RECORD_ID)).isEqualTo(record);

        verify(borrowRecordRepository).findById(MOCK_BORROW_RECORD_ID);
    }

    @Test
    void getRecordShouldThrowErrorWhenBorrowRecordNotFound(){
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BorrowRecordNotFoundException.class, () -> borrowRecordService.getRecord(MOCK_BORROW_RECORD_ID));

        verify(borrowRecordRepository).findById(MOCK_BORROW_RECORD_ID);
    }

    @Test
    void returnBookShouldReturnBookSuccessfully(){
        User user = buildUser();
        Book book = buildBook(MOCK_BOOK_AVAILABLE_COPIES);
        BorrowRecord record = buildBorrowRecord(user, book);
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.of(record));

        borrowRecordService.returnBook(MOCK_BORROW_RECORD_ID);

        assertEquals(MOCK_BORROW_RECORD_ID, record.getId());
        assertEquals(BorrowStatus.RETURNED, record.getStatus());
        assertNotNull(record.getReturnDate());
        assertEquals(MOCK_BOOK_AVAILABLE_COPIES+1, book.getAvailableCopies());
        verify(bookRepository).save(ArgumentMatchers.same(book));
        verify(borrowRecordRepository).save(ArgumentMatchers.same(record));
    }

    @Test
    void returnBookShouldThrowErrorWhenBorrowRecordNotFound(){
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BorrowRecordNotFoundException.class, () -> borrowRecordService.returnBook(MOCK_BORROW_RECORD_ID));

        verify(borrowRecordRepository).findById(MOCK_BORROW_RECORD_ID);
        verify(bookRepository, never()).save(isA(Book.class));
        verify(bookRepository, never()).save(isA(Book.class));
    }

    @Test
    void getBookRecordsShouldGetBookRecordsSuccessfully(){
        User user = buildUser();
        Book book = buildBook(MOCK_BOOK_AVAILABLE_COPIES);
        BorrowRecord record = buildBorrowRecord(user, book);
        BorrowRecord record1 = buildBorrowRecord1(user, book);
        List<BorrowRecord> records = List.of(record, record1);
        when(borrowRecordRepository.findByUserEmailAddress(isA(String.class))).thenReturn(records);

        assertThat(borrowRecordService.getBookRecords(MOCK_USER_EMAIL)).isEqualTo(records);

        verify(borrowRecordRepository).findByUserEmailAddress(MOCK_USER_EMAIL);
    }

    private User buildUser(){
        User user = new User("name", MOCK_USER_EMAIL, "address", Role.USER, "password", true);
        user.setId(MOCK_USER_ID);
        return user;
    }

    private Book buildBook(int copies){
        Book book = new Book("Book Title", "Book Author", "Book ISBN", 450, copies);
        book.setId(MOCK_BOOK_ID);
        return book;
    }

    private BorrowRecord buildBorrowRecord(User user, Book book){
        BorrowRecord record = new BorrowRecord(user, book, LocalDateTime.now(), null, BorrowStatus.BORROWED);
        record.setId(MOCK_BORROW_RECORD_ID);
        return record;
    }

    private BorrowRecord buildBorrowRecord1(User user, Book book){
        BorrowRecord record = new BorrowRecord(user, book, LocalDateTime.now().minusHours(1), LocalDateTime.now(), BorrowStatus.RETURNED);
        record.setId(MOCK_OTHER_BORROW_RECORD_ID);
        return record;
    }
}