package com.assignment6.secureLibraryManagement.jo;

public record BookResponseJO(
        Long id,
        String title,
        String author,

        String isbn,
        Integer price,
        int availableCopies
) {
}
