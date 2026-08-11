package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(unique = true)
    private String isbn;

    @Positive
    private Integer price;

    private int availableCopies;

    public Book(String title, String author, String isbn, int price, int availableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.availableCopies= availableCopies;

    }
}
