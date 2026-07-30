package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BookRequestJO;
import com.assignment6.secureLibraryManagement.dto.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.exception.BookNotFoundException;
import com.assignment6.secureLibraryManagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public BookResponseJO addBook(BookRequestJO bookRequestJO){
        Book book = new Book();
        book.setAuthor(bookRequestJO.author());
        book.setIsbn(bookRequestJO.isbn());
        book.setTitle(bookRequestJO.title());
        book.setAvailableCopies(bookRequestJO.availableCopies());
        book.setPrice(bookRequestJO.price());
        Book bookSaved = bookRepository.save(book);
        return new BookResponseJO(bookSaved.getId(), bookSaved.getTitle(), bookSaved.getAuthor(), bookSaved.getIsbn(), bookSaved.getPrice(), bookSaved.getAvailableCopies());
    }

    public BookResponseJO updateBook(BookRequestJO bookRequestJO, Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("book not found!"));
        book.setAuthor(bookRequestJO.author());
        book.setIsbn(bookRequestJO.isbn());
        book.setTitle(bookRequestJO.title());
        book.setAvailableCopies(bookRequestJO.availableCopies());
        book.setPrice(bookRequestJO.price());
        Book savedBook = bookRepository.save(book);
        return new BookResponseJO(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.getIsbn(), savedBook.getPrice(), savedBook.getAvailableCopies());
    }

    private boolean validateBookId(Long id){
        return id != null && id >= 0L;
    }

    public void deleteBook(Long id){
        boolean isIdValid = validateBookId(id);
        if(!isIdValid) return;
        boolean isExist = bookRepository.existsById(id);
        if(!isExist) return;
        bookRepository.deleteById(id);
    }

    public List<BookResponseJO> getALlBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream().map((b) -> {
            return new BookResponseJO(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getPrice(), b.getAvailableCopies());
        }).toList();
    }

    public BookResponseJO getBook(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("book not found!"));
            return new BookResponseJO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(), book.getAvailableCopies());
    }
}
