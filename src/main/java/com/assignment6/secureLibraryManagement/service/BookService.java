package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.JO.BookRequestJO;
import com.assignment6.secureLibraryManagement.JO.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.exception.BookNotFoundException;
import com.assignment6.secureLibraryManagement.mapper.BookJOMapper;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    public BookResponseJO addBook(BookRequestJO bookRequestJO);

    public BookResponseJO updateBook(BookRequestJO bookRequestJO, Long id);

    public void deleteBook(Long id);

    public List<BookResponseJO> getALlBooks();

    public BookResponseJO getBook(Long bookId);
}
