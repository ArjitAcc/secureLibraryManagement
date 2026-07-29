package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.BookRequestJO;
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
    public ResponseEntity<Book> createBook(@Valid  @RequestBody BookRequestJO bookRequestJO){
        Book book = bookService.addBook(bookRequestJO);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@Valid @PathVariable Long id, @RequestBody BookRequestJO bookRequestJO){
        Optional<Book> book = bookService.updateBook(bookRequestJO, id);
        return book.map(b -> ResponseEntity.ok(b)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getALlBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId){
        Book book = bookService.getBook(bookId);
        return ResponseEntity.ok(book);
    }
}
