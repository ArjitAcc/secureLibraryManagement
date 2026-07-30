package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.BorrowRecordResponseJO;
import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import com.assignment6.secureLibraryManagement.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final BorrowRecordService borrowRecordService;

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<BorrowRecordResponseJO> createBorrowRecord(@PathVariable("book-id") Long bookId, Principal principal){
        BorrowRecordResponseJO borrowRecordResponse = borrowRecordService.borrowBook(principal.getName(), bookId);
        return ResponseEntity.ok(borrowRecordResponse);
    }

    @PutMapping("/return/{borrow-id}")
    public ResponseEntity<BorrowRecordResponseJO> updateBorrowRecordToReturn(@PathVariable("borrow-id") Long borrowId){
        BorrowRecordResponseJO borrowRecordResponse = borrowRecordService.returnBook(borrowId);
        return ResponseEntity.ok(borrowRecordResponse);
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BorrowRecordResponseJO>> getBorrowBooks(Principal principal){
        List<BorrowRecordResponseJO> borrowBooks = borrowRecordService.getBookRecords(principal.getName());
        return ResponseEntity.ok(borrowBooks);
    }
}
