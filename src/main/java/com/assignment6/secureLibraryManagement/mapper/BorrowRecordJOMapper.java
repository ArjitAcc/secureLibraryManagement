package com.assignment6.secureLibraryManagement.mapper;

import com.assignment6.secureLibraryManagement.entity.BorrowRecord;
import com.assignment6.secureLibraryManagement.jo.BorrowRecordResponseJO;
import org.springframework.stereotype.Component;

@Component
public class BorrowRecordJOMapper {
    public BorrowRecordResponseJO mapToPOJO(BorrowRecord borrowRecord){
        return new BorrowRecordResponseJO(borrowRecord.getId(), borrowRecord.getUser(), borrowRecord.getBook(), borrowRecord.getBorrowDate(), borrowRecord.getReturnDate(), borrowRecord.getStatus());
        
    }
}