package com.assignment6.secureLibraryManagement.JO;

import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.entity.BorrowStatus;
import com.assignment6.secureLibraryManagement.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record BorrowRecordResponseJO(@NotNull
     Long id,

     @NotNull
     User user,

     @NotNull
     Book book,

     @Past
     LocalDateTime borrowDate,

     LocalDateTime returnDate,

     @Enumerated(EnumType.STRING)
     BorrowStatus status) {

}
