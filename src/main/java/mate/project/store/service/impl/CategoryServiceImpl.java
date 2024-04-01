package mate.project.store.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import mate.project.store.entity.Category;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.CategoryMapper;
import mate.project.store.repository.category.CategoryRepository;
import mate.project.store.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.requestToEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category with id: "
                        + id)));
    }

    @Override
    public CategoryDto updateById(Long id, CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.findById(id).isPresent()) {
            Category category = categoryMapper.requestToEntity(categoryRequestDto);
            category.setId(id);
            return categoryMapper.toDto(categoryRepository.save(category));
        }
        throw new EntityNotFoundException("Can't find category with id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Can't find a category with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
