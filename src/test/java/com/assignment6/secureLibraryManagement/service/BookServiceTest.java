package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
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
        Book savedBook = new Book("Book Title", "Book Author", "Book ISBN", 450, 2);
        savedBook.setId(1001L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Long bookId = bookService.addBook(savedBook);

        assertEquals(savedBook.getId(), bookId);
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

    @Test
    void getAllBooksShouldGetAllBooksSuccessfully(){
        List<Book> books = new ArrayList<>();
        Book book1 = new Book("Book1 Title", "Book1 Author", "Book1 ISBN", 340, 2);
        book1.setId(1001L);
        Book book2 = new Book("Book2 Title", "Book2 Author", "Book2 ISBN", 430, 1);
        book2.setId(1001L);
        books.add(book1);
        books.add(book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> booksReturned = bookService.getAllBooks();

        verify(bookRepository).findAll();
        assertEquals(books.size(), booksReturned.size());
        for(int i = 0; i < books.size(); i++){
            assertEquals(books.get(i).getId(), booksReturned.get(i).getId());
        }
    }

    @Test
    void updateBookShouldUpdateBookSuccessfully(){
        Long id = 1001L;
        Book book = new Book("Book Title", "Book Author", "Book ISBN", 450, 2);
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Book updatedBook = new Book("Book Updated Title", "Book Author", "Book ISBN", 800, 1);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        bookService.updateBook(updatedBook, id);

        assertNotNull(updatedBook);
        assertEquals(id, updatedBook.getId());
        verify(bookRepository).save(any(Book.class));
        verify(bookRepository).findById(id);
    }

    @Test
    void getBookShouldGetBookSuccessfully(){
        Long id = 1L;
        Book book = new Book("Book Title", "Book Author", "Book ISBN", 450, 2);
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Book bookReturned = bookService.getBook(id);

        assertEquals(bookReturned.getId(), book.getId());
    }

}