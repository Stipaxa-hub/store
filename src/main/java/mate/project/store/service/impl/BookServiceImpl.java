package mate.project.store.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.book.BookDto;
import mate.project.store.dto.book.BookSearchParametersDto;
import mate.project.store.dto.book.CreateBookRequestDto;
import mate.project.store.entity.Book;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.BookMapper;
import mate.project.store.repository.book.BookRepository;
import mate.project.store.repository.book.BookSpecificationBuilder;
import mate.project.store.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Transactional
    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toEntity(bookRequestDto);
        book.setCategories(bookMapper.mapToCategorySet(bookRequestDto.getCategoriesIds()));
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id)));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookRequestDto) {
        if (bookRepository.findById(id).isPresent()) {
            Book book = bookMapper.toEntity(bookRequestDto);
            book.setId(id);
            book.setCategories(bookMapper.mapToCategorySet(bookRequestDto.getCategoriesIds()));
            return bookMapper.toDto(bookRepository.save(book));
        }
        throw new EntityNotFoundException("Can't find a book with id " + id);
    }

    @Override
    public void deleteById(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Can't find a book with id " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
