package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BookRequestJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookService bookService;
    @Test
    void addBookShouldAddBookSuccessfully(){
        BookRequestJO bookRequestJO = new BookRequestJO();
        bookRequestJO.setTitle("DTO Title");
        bookRequestJO.setAuthor("DTO Author");
        bookRequestJO.setIsbn("DTO ISBN");
        bookRequestJO.setAvailableCopies(1);
        Book savedBook = new Book();
        savedBook.setId(1001L);
        savedBook.setTitle("Book Title");
        savedBook.setAuthor("Book Author");
        savedBook.setIsbn("Book ISBN");
        savedBook.setAvailableCopies(2);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Book book = bookService.addBook(bookRequestJO);

        assertNotNull(savedBook);
        assertEquals(savedBook.getId(), book.getId());
    }

    @Test
    void deleteBookShouldDeleteBookSuccessfully(){
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(anyLong());
        bookService.deleteBook(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteBookShouldDoNothingWhenBookDoesNotExist(){
        Long id = 10L;
        when(bookRepository.existsById(id)).thenReturn(false);
        bookService.deleteBook(id);
        verify(bookRepository, never()).deleteById(id);
    }

    // generally a bad practice to test private methods
    @Test
    void testPrivateMethod_ValidateProductIdIfIdIsValid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Long id = 1L;
        Method validateBookId = BookService.class.getDeclaredMethod("validateBookId", Long.class);
        validateBookId.setAccessible(true);
        boolean isValid = (boolean) validateBookId.invoke(bookService, id);
        assertTrue(isValid);
    }

    @Test
    void testPrivateMethod_ValidateProductIfIdIsInvalid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Long id = -1L;
        Method validateBookId = BookService.class.getDeclaredMethod("validateBookId", Long.class);
        validateBookId.setAccessible(true);
        boolean isValid = (boolean) validateBookId.invoke(bookService, id);
        assertFalse(isValid);
    }

    @Test
    void getAllBooksShouldGetAllBooksSuccessfully(){
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1001L);
        book1.setTitle("Book1 Title");
        book1.setAuthor("Book1 Author");
        book1.setIsbn("Book1 ISBN");
        book1.setAvailableCopies(2);

        Book book2 = new Book();
        book1.setId(1001L);
        book1.setTitle("Book2 Title");
        book1.setAuthor("Book2 Author");
        book1.setIsbn("Book2 ISBN");
        book1.setAvailableCopies(1);

        books.add(book1);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> booksReturned = bookService.getALlBooks();

        assertNotNull(booksReturned);
        assertEquals(books, booksReturned);
        verify(bookRepository).findAll();
    }

    @Test
    void updateBookShouldUpdateBookSuccessfully(){
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        Book book = new Book();
        book.setId(1001L);
        book.setTitle("Book Orignal Title");
        book.setAuthor("Book Author");
        book.setIsbn("Book ISBN");
        book.setAvailableCopies(1);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookRequestJO bookRequestJO = new BookRequestJO();
        bookRequestJO.setTitle("Book Updated Title");
        bookRequestJO.setAuthor("Book Author");
        bookRequestJO.setIsbn("Book ISBN");
        bookRequestJO.setAvailableCopies(1);

        Book savedBook = new Book();
        savedBook.setId(1001L);
        savedBook.setTitle("Book Updated Title");
        savedBook.setAuthor("Book Author");
        savedBook.setIsbn("Book ISBN");
        savedBook.setAvailableCopies(1);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        Optional<Book> updatedBook = bookService.updateBook(bookRequestJO, id);

        assertNotNull(updatedBook);
        assertEquals(savedBook.getId(), updatedBook.get().getId());
        assertEquals(savedBook.getTitle(), updatedBook.get().getTitle());
        assertEquals(savedBook.getAuthor(), updatedBook.get().getAuthor());
        assertEquals(savedBook.getIsbn(), updatedBook.get().getIsbn());
        assertEquals(savedBook.getAvailableCopies(), updatedBook.get().getAvailableCopies());
    }

}