package com.assignment6.secureLibraryManagement.controller;

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

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<BorrowRecord> createBorrowRecord(@PathVariable Long bookId, Principal principal){
        BorrowRecord borrowRecord = borrowRecordService.borrowBook(principal.getName(), bookId);
        return ResponseEntity.ok(borrowRecord);
    }

    @PutMapping("/return/{borrowId}")
    public ResponseEntity<BorrowRecord> updateBorrowRecordToReturn(@PathVariable Long borrowId){
        BorrowRecord borrowRecord = borrowRecordService.returnBook(borrowId);
        return ResponseEntity.ok(borrowRecord);
    }

    @GetMapping("/user/my-books")
    public ResponseEntity<List<BorrowRecord>> getBorrowBooks(Principal principal){
        List<BorrowRecord> borrowBooks = borrowRecordService.getBookRecords(principal.getName());
        return ResponseEntity.ok(borrowBooks);
    }
}
