package com.assignment6.secureLibraryManagement.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private int availableCopies;
}
