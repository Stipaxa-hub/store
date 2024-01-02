package mate.project.store.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.project.store.config.MapperConfig;
import mate.project.store.dto.book.BookDto;
import mate.project.store.dto.book.CreateBookRequestDto;
import mate.project.store.entity.Book;
import mate.project.store.entity.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookRequestDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoriesIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoriesIds(bookDto.getCategoriesIds());
    }

    default Set<Category> mapToCategorySet(Set<Long> categoriesIds) {
        return categoriesIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
