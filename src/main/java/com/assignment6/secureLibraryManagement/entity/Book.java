package com.assignment6.secureLibraryManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$")
    private String author;

    @Column(unique = true)
    private String isbn;

    @Max(value=1000)
    @Positive
    private Integer price;

    @PositiveOrZero
    private int availableCopies;

    public void set() {
    }
}
