package mate.project.store.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import mate.project.store.dto.book.BookDto;
import mate.project.store.dto.book.BookSearchParametersDto;
import mate.project.store.dto.book.CreateBookRequestDto;
import mate.project.store.entity.Category;
import mate.project.store.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new book")
    void save_ValidRequestDto_Success() throws Exception {
        // Given
        Category testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
        Category savedCategory = categoryRepository.save(testCategory);

        CreateBookRequestDto createBookRequestDto = CreateBookRequestDto.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("978-161-729-045-9")
                .price(BigDecimal.valueOf(100.0))
                .description("Test Description")
                .coverImage("Test Cover Image")
                .categoriesIds(Set.of(savedCategory.getId()))
                .build();

        BookDto exceptedBookDto = BookDto.builder()
                .id(1L)
                .title(createBookRequestDto.getTitle())
                .author(createBookRequestDto.getAuthor())
                .isbn(createBookRequestDto.getIsbn())
                .price(createBookRequestDto.getPrice())
                .description(createBookRequestDto.getDescription())
                .coverImage(createBookRequestDto.getCoverImage())
                .categoriesIds(createBookRequestDto.getCategoriesIds())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        // When
        MvcResult result = mockMvc.perform(
                    post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actualBookDto);
        Assertions.assertNotNull(actualBookDto.getId());
        EqualsBuilder.reflectionEquals(exceptedBookDto, actualBookDto, "id");
        EqualsBuilder.reflectionEquals(exceptedBookDto, actualBookDto, "title");
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book-category-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllProducts() throws Exception {
        //Given
        List<BookDto> expected = getBookDtoListMock();

        //When
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(5, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book-category-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get book by id")
    void getBookById_ValidId_ShouldReturnValidBookDto() throws Exception {
        // Given
        Long id = 1L;
        BookDto expectedBookDto = new BookDto(
                id, "Book 1", "Author 1", "34-5346-4h-4561",
                BigDecimal.valueOf(1), "Description 1", "image1", Set.of(1L));
        // When
        MvcResult result = mockMvc.perform(
                get("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        BookDto actulBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        Assertions.assertNotNull(actulBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actulBookDto, "id");
        Assertions.assertEquals(expectedBookDto, actulBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book-category-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book by id")
    void update_ValidRequest_ShouldReturnUpdatedBookDto() throws Exception {
        // Given
        Long id = 2L;
        CreateBookRequestDto updateBookRequestDto = CreateBookRequestDto.builder()
                .title("Updated Title")
                .author("Updated Author")
                .isbn("978-161-729-045-9")
                .price(BigDecimal.valueOf(100))
                .description("Updated Description")
                .coverImage("Updated CoverImage")
                .categoriesIds(Set.of(2L))
                .build();

        BookDto expectedBookDto = BookDto.builder()
                .id(id)
                .title(updateBookRequestDto.getTitle())
                .author(updateBookRequestDto.getAuthor())
                .isbn(updateBookRequestDto.getIsbn())
                .price(updateBookRequestDto.getPrice())
                .description(updateBookRequestDto.getDescription())
                .coverImage(updateBookRequestDto.getCoverImage())
                .categoriesIds(updateBookRequestDto.getCategoriesIds())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/books/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actualBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        Assertions.assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id");
        Assertions.assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book-category-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by id")
    void delete_ValidId_ShouldDeleteCorrectBook() throws Exception {
        // Given
        Long id = 3L;

        // When
        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book-category-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search books by parameters")
    void searchBooks_ValidBookSearchParametersDto_ShouldReturnValidBookDto() throws Exception {
        // Given
        BookDto expectedBookDto = BookDto.builder()
                .id(1L)
                .title("Book 1")
                .author("Author 1")
                .isbn("34-5346-4h-4561")
                .price(BigDecimal.valueOf(1))
                .description("Description 1")
                .coverImage("image1")
                .categoriesIds(Set.of(1L))
                .build();

        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto(
                new String[]{"Author 1"},
                new String[]{""}
        );
        String jsonRequest = objectMapper.writeValueAsString(bookSearchParametersDto);
        // When
        MvcResult result = mockMvc.perform(get("/books/search")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        List<BookDto> actualBookDtoList = Arrays.asList(
                objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class)
        );
        BookDto actualBookDto = actualBookDtoList.get(0);
        Assertions.assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id");
        Assertions.assertEquals(expectedBookDto, actualBookDto);
    }

    private List<BookDto> getBookDtoListMock() {
        List<BookDto> bookDtoList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            BookDto bookDto = BookDto.builder()
                    .id(Long.valueOf(i))
                    .title("Book " + i)
                    .author("Author " + i)
                    .isbn("34-5346-4h-456" + i)
                    .price(BigDecimal.valueOf(i))
                    .description("Description " + i)
                    .coverImage("image" + i)
                    .categoriesIds(Set.of(Long.valueOf(i)))
                    .build();

            bookDtoList.add(bookDto);
        }

        return bookDtoList;
    }
}
    
