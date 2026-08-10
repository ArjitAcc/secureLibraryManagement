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

    public Long addBook(Book book){
        Book bookSaved = bookRepository.save(book);
        return bookSaved.getId();
    }

    public void updateBook(Book book, Long id){
        getBook(id);
        book.setId(id);
        bookRepository.save(book);
    }

    public void deleteBook(Long id){
        if(bookRepository.existsById(id)) bookRepository.deleteById(id);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBook(Long bookId){
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("book not found!"));
    }
}
