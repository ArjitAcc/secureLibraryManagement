package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BookRequestJO;
import com.assignment6.secureLibraryManagement.dto.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
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

    public Optional<BookResponseJO> updateBook(BookRequestJO bookRequestJO, Long id){

        boolean isExist = bookRepository.existsById(id);
        if(!isExist) return Optional.empty();
        Optional<Book> book = bookRepository.findById(id);
        Optional<BookResponseJO> bookResponseObject = book.map(b -> {
            b.setAuthor(bookRequestJO.author());
            b.setIsbn(bookRequestJO.isbn());
            b.setTitle(bookRequestJO.title());
            b.setAvailableCopies(bookRequestJO.availableCopies());
            b.setPrice(bookRequestJO.price());
            Book savedBook = bookRepository.save(b);
            return new BookResponseJO(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(), savedBook.getIsbn(), savedBook.getPrice(), savedBook.getAvailableCopies());
        });
        return Optional.empty();
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
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<BookResponseJO> bookResponseObject = book.map((b) -> {
            return new BookResponseJO(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getPrice(), b.getAvailableCopies());
        });
        return bookResponseObject.orElse(null);
    }
}
