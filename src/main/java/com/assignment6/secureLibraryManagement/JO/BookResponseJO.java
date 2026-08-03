package com.assignment6.secureLibraryManagement.JO;

public record BookResponseJO(
        Long id,
        String title,
        String author,

        String isbn,
        Integer price,
        int availableCopies
) {
}
