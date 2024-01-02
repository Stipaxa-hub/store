package mate.project.store.service;

import java.util.List;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryRequestDto categoryRequestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto updateById(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
