package com.assignment6.secureLibraryManagement.controller;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// For HTTP Methods (get, post, put, delete, etc.)

// For checking HTTP Status (status().isOk(), status().isNotFound(), etc.)

// For checking the response content or JSON properties


@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private BookService bookService;
//
//    Book book = new Book();
//    BookRequestJO bookRequestJO = new BookRequestJO();
//
//    @BeforeEach
//    void init(){
//        book.setId(1001L);
//        book.setTitle("Book Title");
//        book.setAuthor("Book Author");
//        book.setIsbn("Book ISBN");
//        book.setAvailableCopies(2);
//        bookRequestJO.setTitle("Book Title");
//        bookRequestJO.setAuthor("Book Author");
//        bookRequestJO.setIsbn("Book ISBN");
//        bookRequestJO.setAvailableCopies(2);
//    }
//
//    @Test
//    void getAllBooksShouldGetAllBooksSuccessfully() throws Exception {
//        List<Book> books = new ArrayList<>();
//        Book book1 = new Book();
//        book1.setId(1001L);
//        book1.setTitle("Book1 Title");
//        book1.setAuthor("Book1 Author");
//        book1.setIsbn("Book1 ISBN");
//        book1.setAvailableCopies(3);
//
//        books.add(book);
//        books.add(book1);
//
//        String expectedJson = objectMapper.writeValueAsString(books);
//
//        when(bookService.getALlBooks()).thenReturn(books);
//
//        mockMvc.perform(get("/admin/books"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//    }
//
//    @Test
//    void deleteBookShouldDeleteBookSuccessfully() throws Exception {
//        Long id = 1L;
//        doNothing().when(bookService).deleteBook(id);
//        mockMvc.perform(delete("/admin/books/"+id))
//                .andExpect(status().isNoContent());
//
//    }
//
//    @Test
//    void createBookShouldCreateBookSuccessfully() throws Exception {
//        String requestJson = objectMapper.writeValueAsString(bookRequestJO);
//        String expectedJson = objectMapper.writeValueAsString(book);
//        when(bookService.addBook(bookRequestJO)).thenReturn(book);
//        mockMvc.perform(post("/admin/books")
//                        .content(requestJson)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//    }
//
//    @Test
//    void updateBookShouldUpdateBookSuccessfully() throws Exception {
//        String requestJson = objectMapper.writeValueAsString(bookRequestJO);
//        Long id = 1001L;
//        book.setTitle("Updated Book Title");
//        String expectedJson = objectMapper.writeValueAsString(book);
//        when(bookService.updateBook(bookRequestJO, id)).thenReturn(Optional.of(book));
//        mockMvc.perform(put("/admin/books/"+id)
//                        .content(requestJson)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//    }
}