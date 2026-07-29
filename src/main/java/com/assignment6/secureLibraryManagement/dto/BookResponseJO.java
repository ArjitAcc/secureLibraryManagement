package com.assignment6.secureLibraryManagement.dto;

import jakarta.validation.constraints.*;

public record BookResponseJO(
        Long id,
        String title,
        String author,

        String isbn,
        Integer price,
        int availableCopies
) {
}
