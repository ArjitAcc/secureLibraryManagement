package com.assignment6.secureLibraryManagement.JO;


import jakarta.validation.constraints.*;

public record BookRequestJO(
        @NotBlank
        String title,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]+$")
        String author,

        String isbn,

        @Max(value = 1000)
        @Positive
        Integer price,

        @PositiveOrZero
        int availableCopies
) { }
