package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.BookRequestDto;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.service.BookService;
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
    public ResponseEntity<Book> createBook(@RequestBody BookRequestDto bookRequestDto){
        Book book = bookService.addBook(bookRequestDto);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookRequestDto){
        Optional<Book> book = bookService.updateBook(bookRequestDto, id);
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
}
