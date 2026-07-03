package com.assignment6.secureLibraryManagement.controller;

import com.assignment6.secureLibraryManagement.dto.BookRequestDto;
import com.assignment6.secureLibraryManagement.entity.Book;
import com.assignment6.secureLibraryManagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
// For HTTP Methods (get, post, put, delete, etc.)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// For checking HTTP Status (status().isOk(), status().isNotFound(), etc.)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// For checking the response content or JSON properties
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    Book book = new Book();
    BookRequestDto bookRequestDto = new BookRequestDto();

    @BeforeEach
    void init(){
        book.setId(1001L);
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setIsbn("Book ISBN");
        book.setAvailableCopies(2);
        bookRequestDto.setTitle("Book Title");
        bookRequestDto.setAuthor("Book Author");
        bookRequestDto.setIsbn("Book ISBN");
        bookRequestDto.setAvailableCopies(2);
    }

    @Test
    void getAllBooksShouldGetAllBooksSuccessfully() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1001L);
        book1.setTitle("Book1 Title");
        book1.setAuthor("Book1 Author");
        book1.setIsbn("Book1 ISBN");
        book1.setAvailableCopies(3);

        books.add(book);
        books.add(book1);

        String expectedJson = objectMapper.writeValueAsString(books);

        when(bookService.getALlBooks()).thenReturn(books);

        mockMvc.perform(get("/admin/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void deleteBookShouldDeleteBookSuccessfully() throws Exception {
        Long id = 1L;
        doNothing().when(bookService).deleteBook(id);
        mockMvc.perform(delete("/admin/books/"+id))
                .andExpect(status().isNoContent());

    }

    @Test
    void createBookShouldCreateBookSuccessfully() throws Exception {
        String requestJson = objectMapper.writeValueAsString(bookRequestDto);
        String expectedJson = objectMapper.writeValueAsString(book);
        when(bookService.addBook(bookRequestDto)).thenReturn(book);
        mockMvc.perform(post("/admin/books")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void updateBookShouldUpdateBookSuccessfully() throws Exception {
        String requestJson = objectMapper.writeValueAsString(bookRequestDto);
        Long id = 1001L;
        book.setTitle("Updated Book Title");
        String expectedJson = objectMapper.writeValueAsString(book);
        when(bookService.updateBook(bookRequestDto, id)).thenReturn(Optional.of(book));
        mockMvc.perform(put("/admin/books/"+id)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}