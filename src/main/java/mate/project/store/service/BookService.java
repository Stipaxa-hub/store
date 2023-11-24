package mate.project.store.service;

import java.util.List;
import mate.project.store.dto.BookDto;
import mate.project.store.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
