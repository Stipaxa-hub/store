package mate.project.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import mate.project.store.entity.Category;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.CategoryMapper;
import mate.project.store.repository.category.CategoryRepository;
import mate.project.store.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Testing save category with valid dto")
    void saveCategory_WithValidCreateCategoryRequestDto_ShouldReturnValidCategoryDto() {
        Category category = createDefaultCategory();
        CategoryRequestDto categoryRequestDto = createDefaultCategoryRequestDto(category);
        CategoryDto categoryDto = createDefaultCategoryDto(category);

        when(categoryMapper.requestToEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actualCategoryDto = categoryService.save(categoryRequestDto);

        assertNotNull(actualCategoryDto);
        assertEquals(categoryDto, actualCategoryDto);

        verify(categoryMapper, times(1)).requestToEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Testing to get all books")
    void findAll_ValidParam_ShouldReturnValidList() {
        List<Category> categories = createDefaultCategoriesList();
        List<CategoryDto> categoriesDto = createDefaultCategoriesDtoList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(categories.get(0))).thenReturn(categoriesDto.get(0));
        when(categoryMapper.toDto(categories.get(1))).thenReturn(categoriesDto.get(1));
        when(categoryMapper.toDto(categories.get(2))).thenReturn(categoriesDto.get(2));

        List<CategoryDto> actualListCategoryDto = categoryService.findAll(pageable);

        assertNotNull(actualListCategoryDto);
        assertEquals(categoriesDto, actualListCategoryDto);

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(categories.get(0));
        verify(categoryMapper, times(1)).toDto(categories.get(1));
        verify(categoryMapper, times(1)).toDto(categories.get(2));
    }

    @Test
    @DisplayName("Testing to valid get category by id")
    void findById_WithValidId_ShouldReturnValidCategoryDto() {
        Long categoryId = 1L;
        Category category = createDefaultCategory();
        CategoryDto categoryDto = createDefaultCategoryDto(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actualCategoryDto = categoryService.findById(categoryId);

        assertNotNull(actualCategoryDto);
        assertEquals(actualCategoryDto, categoryDto);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Testing to find by invalid id")
    void findById_WithNotValid_ShouldThrowException() {
        Long categoryId = 0L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, (() -> categoryService.findById(categoryId)));
    }

    @Test
    @DisplayName("Testing to update category by valid id")
    void updateById_WithValidId_ShouldUpdateCategory() {
        Long categoryId = 1L;

        Category oldCategory = createDefaultCategory();

        Category updatedCategory = createDefaultCategory();
        updatedCategory.setName("Updated Category");
        CategoryDto updatedCategoryDto = createDefaultCategoryDto(updatedCategory);

        CategoryRequestDto categoryRequestDto = createDefaultCategoryRequestDto(updatedCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));
        when(categoryMapper.requestToEntity(categoryRequestDto)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);

        CategoryDto actualCategoryDto = categoryService.updateById(categoryId, categoryRequestDto);

        assertNotNull(actualCategoryDto);
        assertEquals(updatedCategoryDto, actualCategoryDto);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).requestToEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(updatedCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Testing to update category with not valid id")
    void updateById_NotValidId_ShouldThrowException() {
        Long categoryId = 100L;

        CategoryRequestDto categoryRequestDto =
                createDefaultCategoryRequestDto(createDefaultCategory());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                (() -> categoryService.updateById(categoryId, categoryRequestDto)));
    }

    @Test
    @DisplayName("Testing to delete valid id")
    void deleteById_ValidId_ShouldDelete() {
        Long categoryId = 1L;
        Category category = createDefaultCategory();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Testing to delete category by invalid id")
    void deleteById_NotValidId_ShouldThrowException() {
        Long categoryId = 99L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, (() -> categoryService.deleteById(categoryId)));

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    public static Category createDefaultCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name For Test");
        category.setDescription("Category Description For Test");
        return category;
    }

    private CategoryDto createDefaultCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private CategoryRequestDto createDefaultCategoryRequestDto(Category category) {
        return new CategoryRequestDto(
                category.getName(),
                category.getDescription()
        );
    }

    private List<Category> createDefaultCategoriesList() {
        List<Category> categories = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Category category = new Category();
            category.setId(Long.valueOf(i));
            category.setName("Category" + i);
            category.setDescription("Description" + i);
            categories.add(category);
        }
        return categories;
    }

    private static List<CategoryDto> createDefaultCategoriesDtoList() {
        List<CategoryDto> categories = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            CategoryDto categoryDto = new CategoryDto(
                    Long.valueOf(i),
                    "Category" + i,
                    "Description" + i
            );
            categories.add(categoryDto);
        }

        return categories;
    }
}
