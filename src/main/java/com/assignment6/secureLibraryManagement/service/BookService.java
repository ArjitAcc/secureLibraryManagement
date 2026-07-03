package com.assignment6.secureLibraryManagement.service;

import com.assignment6.secureLibraryManagement.dto.BookRequestDto;
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
    public Book addBook(BookRequestDto bookRequestDto){
        Book book = new Book();
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setTitle(bookRequestDto.getTitle());
        book.setAvailableCopies(bookRequestDto.getAvailableCopies());
        Book bookSaved = bookRepository.save(book);
        return bookSaved;
    }
    public Optional<Book> updateBook(BookRequestDto bookRequestDto, Long id){

        boolean isExist = bookRepository.existsById(id);
        if(!isExist) return Optional.empty();
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(b -> {
            b.setAuthor(bookRequestDto.getAuthor());
            b.setIsbn(bookRequestDto.getIsbn());
            b.setTitle(bookRequestDto.getTitle());
            b.setAvailableCopies(bookRequestDto.getAvailableCopies());
            bookRepository.save(b);
        });
        return book;
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

    public List<Book> getALlBooks(){
        List<Book> books = bookRepository.findAll();
        return books;
    }
}
