package mate.project.store.util;

import java.util.ArrayList;
import java.util.List;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import mate.project.store.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class TestCategoryProvider {
    public static Category createDefaultCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name For Test");
        category.setDescription("Category Description For Test");
        return category;
    }

    public static List<Category> createDefaultCategoriesList() {
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

    public static List<CategoryDto> createDefaultCategoriesDtoList() {
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

    public static CategoryRequestDto createDefaultCategoryRequestDto(Category category) {
        return new CategoryRequestDto(
                category.getName(),
                category.getDescription()
        );
    }

    public static CategoryDto createDefaultCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
