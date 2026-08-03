package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.JO.BookRequestJO;
import com.assignment6.secureLibraryManagement.JO.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import static org.junit.jupiter.api.Assertions.*;

import com.assignment6.secureLibraryManagement.service.impl.BookServiceImpl;
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
    BookServiceImpl bookService;
    @Test
    void addBookShouldAddBookSuccessfully(){
        BookRequestJO bookRequestJO = new BookRequestJO("DTO Title", "DTO Author", "DTO ISBN", 800, 2);
        Book savedBook = new Book();
        savedBook.setId(1001L);
        savedBook.setTitle("Book Title");
        savedBook.setAuthor("Book Author");
        savedBook.setIsbn("Book ISBN");
        savedBook.setPrice(450);
        savedBook.setAvailableCopies(2);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponseJO book = bookService.addBook(bookRequestJO);

        assertNotNull(savedBook);
        assertEquals(savedBook.getId(), book.id());
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
        book1.setPrice(340);
        book1.setAvailableCopies(2);

        Book book2 = new Book();
        book1.setId(1001L);
        book1.setTitle("Book2 Title");
        book1.setAuthor("Book2 Author");
        book1.setIsbn("Book2 ISBN");
        book1.setPrice(430);
        book1.setAvailableCopies(1);

        books.add(book1);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponseJO> booksReturned = bookService.getALlBooks();

        verify(bookRepository).findAll();
        assertNotNull(booksReturned);
        assertEquals(books.size(), booksReturned.size());
        for(int i = 0; i < books.size(); i++){
            assertEquals(books.get(i).getId(), booksReturned.get(i).id());
        }
    }

    @Test
    void updateBookShouldUpdateBookSuccessfully(){
        Long id = 1L;
        Book book = new Book();
        book.setId(1001L);
        book.setTitle("Book Orignal Title");
        book.setAuthor("Book Author");
        book.setIsbn("Book ISBN");
        book.setPrice(400);
        book.setAvailableCopies(1);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookRequestJO bookRequestJO = new BookRequestJO("Book Updated Title", "Book Author", "Book ISBN", 800, 1);

        Book savedBook = new Book();
        savedBook.setId(1001L);
        savedBook.setTitle("Book Updated Title");
        savedBook.setAuthor("Book Author");
        savedBook.setIsbn("Book ISBN");
        book.setPrice(800);
        savedBook.setAvailableCopies(1);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        BookResponseJO updatedBook = bookService.updateBook(bookRequestJO, id);

        assertNotNull(updatedBook);
        assertEquals(savedBook.getId(), updatedBook.id());
        assertEquals(savedBook.getTitle(), updatedBook.title());
        assertEquals(savedBook.getAuthor(), updatedBook.author());
        assertEquals(savedBook.getIsbn(), updatedBook.isbn());
        assertEquals(savedBook.getPrice(), updatedBook.price());
        assertEquals(savedBook.getAvailableCopies(), updatedBook.availableCopies());
    }

    @Test
    void getBookShouldGetBookSuccessfully(){
        Long id = 1L;
        Book book = new Book();
        book.setId(1001L);
        book.setTitle("Book1 Title");
        book.setAuthor("Book1 Author");
        book.setIsbn("Book1 ISBN");
        book.setPrice(340);
        book.setAvailableCopies(2);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        BookResponseJO bookReturned = bookService.getBook(id);
        assertEquals(bookReturned.id(), book.getId());
    }

}