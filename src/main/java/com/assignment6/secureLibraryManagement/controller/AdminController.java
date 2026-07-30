package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.BookRequestJO;
import com.assignment6.secureLibraryManagement.dto.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<BookResponseJO> createBook(@Valid  @RequestBody BookRequestJO bookRequestJO){
        BookResponseJO book = bookService.addBook(bookRequestJO);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/books/{book-id}")
    public ResponseEntity<BookResponseJO> updateBook(@Valid @PathVariable("book-id") Long bookId, @RequestBody BookRequestJO bookRequestJO){
        Optional<BookResponseJO> book = bookService.updateBook(bookRequestJO, bookId);
        return book.map(b -> ResponseEntity.ok(b)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseJO>> getAllBooks(){
        List<BookResponseJO> books = bookService.getALlBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{book-id}")
    public ResponseEntity<BookResponseJO> getBook(@PathVariable("book-id") Long bookId){
        BookResponseJO book = bookService.getBook(bookId);
        return ResponseEntity.ok(book);
    }
}
