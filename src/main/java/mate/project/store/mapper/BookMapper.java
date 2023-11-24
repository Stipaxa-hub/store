package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.BookDto;
import mate.project.store.dto.CreateBookRequestDto;
import mate.project.store.entity.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookRequestDto);
}
