package mate.project.store.service;

import java.util.List;
import mate.project.store.dto.BookDto;
import mate.project.store.dto.BookSearchParametersDto;
import mate.project.store.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
