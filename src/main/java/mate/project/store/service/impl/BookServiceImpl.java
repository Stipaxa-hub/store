package mate.project.store.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.BookDto;
import mate.project.store.dto.CreateBookRequestDto;
import mate.project.store.entity.Book;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.BookMapper;
import mate.project.store.repository.BookRepository;
import mate.project.store.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toEntity(bookRequestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id)));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookRequestDto) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book with id " + id));
        Book book = bookMapper.toEntity(bookRequestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
