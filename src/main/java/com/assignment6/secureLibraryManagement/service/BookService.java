package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;

import java.util.List;

public interface BookService {

    Long addBook(Book book);

    void updateBook(Book book, Long id);

    void deleteBook(Long id);

    List<Book> getAllBooks();

    Book getBook(Long bookId);
}
