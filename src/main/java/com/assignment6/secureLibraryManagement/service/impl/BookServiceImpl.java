package com.assignment6.secureLibraryManagement.service.impl;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.exception.BookNotFoundException;
import com.assignment6.secureLibraryManagement.mapper.BookJOMapper;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import com.assignment6.secureLibraryManagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookJOMapper bookJOMapper;
    public BookResponseJO addBook(Book book){
        Book bookSaved = bookRepository.save(book);
        return bookJOMapper.mapToPOJO(bookSaved);
    }

    public BookResponseJO updateBook(BookRequestJO bookRequestJO, Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("book not found!"));
        bookJOMapper.mapFromJO(bookRequestJO, book);
        Book savedBook = bookRepository.save(book);
        return bookJOMapper.mapToPOJO(savedBook);
    }

    public void deleteBook(Long id){
        boolean isExist = bookRepository.existsById(id);
        if(!isExist) return;
        bookRepository.deleteById(id);
    }

    public List<BookResponseJO> getALlBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream().map((b) -> bookJOMapper.mapToPOJO(b)).toList();
    }

    public BookResponseJO getBook(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("book not found!"));
        return bookJOMapper.mapToPOJO(book);
    }
}
