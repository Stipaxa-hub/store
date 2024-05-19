package mate.project.store.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    @Sql(scripts = {"classpath:database/categories/"
                    + "remove-mock-categories-from-categories-table.sql",
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create a new category")
    @Order(1)
    void save_ValidRequestDto_Success() throws Exception {
        // Given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                "Test Category", "Test Description"
        );
        CategoryDto expectedCategoryDto = new CategoryDto(
                1L, categoryRequestDto.name(), categoryRequestDto.description()
        );
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "id");
        Assertions.assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @WithMockUser(username = "user")
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/"
                    + "remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Find all categories")
    void findAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        // Given
        List<CategoryDto> expectedCategoryDto = getMockCategoriesDtoList();

        // When
        MvcResult result = mockMvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto[] actualCategoryDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class
        );
        Assertions.assertNotNull(actualCategoryDto);
        Assertions.assertEquals(expectedCategoryDto.size(), actualCategoryDto.length);
        Assertions.assertEquals(expectedCategoryDto, Arrays.stream(actualCategoryDto).toList());
    }

    @WithMockUser(username = "user")
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/"
            + "remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Find by id")
    void findById_ValidId_ShouldReturnValidCategoryDto() throws Exception {
        // Given
        Long id = 1L;
        CategoryDto expectedCategoryDto = new CategoryDto(id, "Category 1", "Description 1");

        // When
        MvcResult result = mockMvc.perform(
                get("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "id");
        Assertions.assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/"
            + "remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Update by id")
    void updateById_ValidRequest_ShouldReturnValidUpdatedCategoryDto() throws Exception {
        // Given
        Long id = 2L;
        CategoryRequestDto updateRequestDto = new CategoryRequestDto(
                "Updated Category Name", "Updated Category Description");
        CategoryDto expectedCategoryDto = new CategoryDto(id, updateRequestDto.name(), updateRequestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                put("/categories/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "id");
        Assertions.assertEquals(expectedCategoryDto, actualCategoryDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/"
            + "remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete by id")
    void deleteById_ValidId_ShouldDeleteCorrectCategory() throws Exception {
        // Given
        Long id = 3L;

        // When
        mockMvc.perform(
                delete("/categories/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private List<CategoryDto> getMockCategoriesDtoList() {
        List<CategoryDto> mockCategoryDto = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            CategoryDto categoryDto = new CategoryDto(
                    Long.valueOf(i), "Category " + i, "Description " + i
            );
            mockCategoryDto.add(categoryDto);
        }

        return mockCategoryDto;
    }
}
