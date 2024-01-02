package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.category.CategoryDto;
import mate.project.store.dto.category.CategoryRequestDto;
import mate.project.store.entity.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Category requestToEntity(CategoryRequestDto categoryRequestDto);
}
