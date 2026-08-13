package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.jo.BorrowRecordResponseJO;
import com.assignment6.secureLibraryManagement.mapper.BorrowRecordJOMapper;
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
    private final BorrowRecordJOMapper borrowRecordJOMapper;

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> createBorrowRecord(@PathVariable("book-id") Long bookId, Principal principal){
        return ResponseEntity.ok(borrowRecordService.borrowBook(principal.getName(), bookId));
    }

    @PutMapping("/return/{borrow-id}")
    public ResponseEntity<Void> updateBorrowRecordToReturn(@PathVariable("borrow-id") Long borrowId){
        borrowRecordService.returnBook(borrowId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{record-id}")
    public ResponseEntity<BorrowRecordResponseJO> getRecord(@PathVariable("record-id") Long recordId){
        return ResponseEntity.ok(borrowRecordJOMapper.mapToPOJO(borrowRecordService.getRecord(recordId)));
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BorrowRecordResponseJO>> getBorrowBooks(Principal principal){
        return ResponseEntity.ok(borrowRecordService.getBookRecords(principal.getName()).stream().map(borrowRecordJOMapper::mapToPOJO).toList());
    }
}
