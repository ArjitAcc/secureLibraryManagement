package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;

import java.util.List;

public interface BookService {
    public BookResponseJO addBook(Book book);

    public BookResponseJO updateBook(BookRequestJO bookRequestJO, Long id);

    public void deleteBook(Long id);

    public List<BookResponseJO> getALlBooks();

    public BookResponseJO getBook(Long bookId);
}
