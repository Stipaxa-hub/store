package mate.project.store.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void save_ValidRequestDto_Success() throws Exception {
        // Given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                "Test Category", "Test Description"
        );
        CategoryDto exceptedCategoryDto = new CategoryDto(
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
        Assertions.assertNotNull(actualCategoryDto.id());
        EqualsBuilder.reflectionEquals(exceptedCategoryDto, actualCategoryDto, "id");
        EqualsBuilder.reflectionEquals(exceptedCategoryDto, actualCategoryDto, "title");
    }

    @WithMockUser(username = "user")
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/"
                    + "remove-mock-categories-from-categories-table.sql",
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Find all categories")
    void findAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        // Given
        List<CategoryDto> exceptedCategoryDto = getMockCategoriesDtoList();

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
        Assertions.assertEquals(exceptedCategoryDto.size(), actualCategoryDto.length);
        Assertions.assertEquals(exceptedCategoryDto, Arrays.stream(actualCategoryDto).toList());
    }

    private List<CategoryDto> getMockCategoriesDtoList() {
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            CategoryDto categoryDto = new CategoryDto(
                    Long.valueOf(i), "Category " + i, "Description " + i
            );
            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }
}
