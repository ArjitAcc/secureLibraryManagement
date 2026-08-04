package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;

import java.util.List;

public interface BookService {
    BookResponseJO addBook(Book book);

    BookResponseJO updateBook(BookRequestJO bookRequestJO, Long id);

    void deleteBook(Long id);

    List<BookResponseJO> getALlBooks();

    BookResponseJO getBook(Long bookId);
}
