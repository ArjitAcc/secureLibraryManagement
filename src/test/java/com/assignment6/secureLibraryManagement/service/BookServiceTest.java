package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.exception.BookNotFoundException;
import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.repository.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.assignment6.secureLibraryManagement.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

    long MOCK_BOOK_ID = 1001L;
    long MOCK_OTHER_BOOK_ID = 1002L;

    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookServiceImpl bookService;

    @Test
    void addBookShouldAddBookSuccessfully(){

        Book book = buildBook1(null);
        when(bookRepository.save(book)).thenAnswer(invocation -> {
            book.setId(MOCK_BOOK_ID);
            return book;
        });

        assertThat(bookService.addBook(book)).isEqualTo(MOCK_BOOK_ID);
        verify(bookRepository).save(ArgumentMatchers.same(book));
    }

    @Test
    void deleteBookShouldDeleteBookSuccessfully(){
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        bookService.deleteBook(MOCK_BOOK_ID);

        verify(bookRepository).existsById(MOCK_BOOK_ID);
        verify(bookRepository).deleteById(MOCK_BOOK_ID);
    }

    @Test
    void deleteBookShouldDoNothingWhenBookDoesNotExist(){
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        bookService.deleteBook(MOCK_BOOK_ID);

        verify(bookRepository).existsById(MOCK_BOOK_ID);
        verify(bookRepository, never()).deleteById(MOCK_BOOK_ID);
    }

    @Test
    void getAllBooksShouldGetAllBooksSuccessfully(){
        List<Book> books = List.of(buildBook1(MOCK_BOOK_ID), buildBook2(MOCK_OTHER_BOOK_ID));
        when(bookRepository.findAll()).thenReturn(books);

        assertThat(bookService.getAllBooks()).isEqualTo(books);

        verify(bookRepository).findAll();
    }

    @Test
    void updateBookShouldUpdateBookSuccessfully(){
        Book book = buildBook1(MOCK_BOOK_ID);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        Book updatedBook = buildBook2(MOCK_BOOK_ID);
        when(bookRepository.save(isA(Book.class))).thenReturn(updatedBook);

        bookService.updateBook(updatedBook, MOCK_BOOK_ID);

        assertEquals(MOCK_BOOK_ID, updatedBook.getId());
        verify(bookRepository).save(ArgumentMatchers.same(updatedBook));
        verify(bookRepository).findById(MOCK_BOOK_ID);
    }

    @Test
    void updateBookShouldThrowErrorWhenBookDoesNotExist(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Book updatedBook = buildBook2(MOCK_BOOK_ID);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(updatedBook, MOCK_BOOK_ID));

        verify(bookRepository, never()).save(updatedBook);
        verify(bookRepository).findById(MOCK_BOOK_ID);
    }

    @Test
    void getBookShouldGetBookSuccessfully(){
        Book book = buildBook1(MOCK_BOOK_ID);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertThat(bookService.getBook(MOCK_BOOK_ID)).isEqualTo(book);

        verify(bookRepository).findById(MOCK_BOOK_ID);
    }

    @Test
    void getBookShouldThrowErrorWhenBookDoesNotExist(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBook(MOCK_BOOK_ID));

        verify(bookRepository).findById(MOCK_BOOK_ID);
    }


    private static Book buildBook1(Long id){
        Book book1 = new Book("Book1 Title", "Book1 Author", "Book1 ISBN", 340, 2);
        book1.setId(id);
        return book1;
    }

    private Book buildBook2(Long id){
        Book book2 = new Book("Book2 Title", "Book2 Author", "Book2 ISBN", 430, 1);
        book2.setId(id);
        return book2;
    }

}