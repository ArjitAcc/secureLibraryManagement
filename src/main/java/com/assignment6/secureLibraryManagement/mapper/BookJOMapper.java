package com.assignment6.secureLibraryManagement.mapper;

import com.assignment6.secureLibraryManagement.JO.BookRequestJO;
import com.assignment6.secureLibraryManagement.JO.BookResponseJO;
import com.assignment6.secureLibraryManagement.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookJOMapper {
    public void mapFromJO(BookRequestJO bookRequestJO, Book book){
        book.setAuthor(bookRequestJO.author());
        book.setIsbn(bookRequestJO.isbn());
        book.setTitle(bookRequestJO.title());
        book.setAvailableCopies(bookRequestJO.availableCopies());
        book.setPrice(bookRequestJO.price());
    }
    
    public BookResponseJO mapToPOJO(Book book){
        return new BookResponseJO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(), book.getAvailableCopies());
    }

}
