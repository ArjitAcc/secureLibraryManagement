package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.jo.BookRequestJO;
import com.assignment6.secureLibraryManagement.jo.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.mapper.BookJOMapper;
import com.assignment6.secureLibraryManagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookService bookService;
    public final BookJOMapper bookJOMapper;

    @PostMapping("/books")
    public ResponseEntity<Long> createBook(@Valid  @RequestBody BookRequestJO bookRequestJO){
        Book book = new Book();
        bookJOMapper.mapFromJO(bookRequestJO, book);
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @PutMapping("/books/{book-id}")
    public ResponseEntity<Void> updateBook(@Valid @PathVariable("book-id") Long bookId, @RequestBody BookRequestJO bookRequestJO){
        Book book = new Book();
        bookJOMapper.mapFromJO(bookRequestJO, book);
        bookService.updateBook(book, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseJO>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks().stream().map(bookJOMapper::mapToPOJO).toList());
    }

    @GetMapping("/books/{book-id}")
    public ResponseEntity<BookResponseJO> getBook(@PathVariable("book-id") Long bookId){
        return ResponseEntity.ok(bookJOMapper.mapToPOJO(bookService.getBook(bookId)));
    }
}
