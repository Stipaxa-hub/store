package mate.project.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.project.store.dto.book.BookDto;
import mate.project.store.dto.book.CreateBookRequestDto;
import mate.project.store.entity.Book;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.BookMapper;
import mate.project.store.repository.book.BookRepository;
import mate.project.store.service.impl.BookServiceImpl;
import mate.project.store.util.TestBookProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Testing save book with valid dto")
    void saveBook_WithValidCreateBookRequestDto_ShouldReturnValidBookDto() {
        Book book = TestBookProvider.createDefaultBook();
        CreateBookRequestDto bookRequestDto = TestBookProvider.createDefaultBookRequestDto(book);
        BookDto bookDto = TestBookProvider.createDefaultBookDto(book);

        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actualBookDto = bookService.save(bookRequestDto);

        assertNotNull(actualBookDto);
        assertEquals(bookDto, actualBookDto);

        verify(bookMapper, times(1)).toEntity(bookRequestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Testing to get all books dto")
    void findAll_ValidParam_ShouldReturnValidList() {
        List<Book> books = TestBookProvider.createDefaultListBook();
        List<BookDto> booksDto = TestBookProvider.createDefaultListBookDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(books.get(0))).thenReturn(booksDto.get(0));
        when(bookMapper.toDto(books.get(1))).thenReturn(booksDto.get(1));
        when(bookMapper.toDto(books.get(2))).thenReturn(booksDto.get(2));

        List<BookDto> actualListBookDto = bookService.findAll(pageable);

        assertNotNull(actualListBookDto);
        assertEquals(booksDto, actualListBookDto);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(books.get(0));
        verify(bookMapper, times(1)).toDto(books.get(1));
        verify(bookMapper, times(1)).toDto(books.get(2));
    }

    @Test
    @DisplayName("Testing to valid get book by id")
    void findById_WithValidId_ShouldReturnValidBookDto() {
        Long bookId = 1L;
        Book book = TestBookProvider.createDefaultBook();
        BookDto bookDto = TestBookProvider.createDefaultBookDto(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actualBookDto = bookService.findById(bookId);

        assertNotNull(actualBookDto);
        assertEquals(actualBookDto, bookDto);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Testing to find by invalid id")
    void findById_WithNotValid_ShouldThrowException() {
        Long bookId = 0L;
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, (() -> bookService.findById(bookId)));
    }

    @Test
    @DisplayName("Testing to update book by valid id")
    void updateById_ValidId_ShouldUpdateBook() {
        Long bookId = 1L;

        Book oldBook = TestBookProvider.createDefaultBook();

        Book updatedBook = TestBookProvider.createDefaultBook();
        updatedBook.setTitle("UpdatedBook");
        BookDto updatedBookDto = TestBookProvider.createDefaultBookDto(updatedBook);

        CreateBookRequestDto updatedRequestBookDto =
                TestBookProvider.createDefaultBookRequestDto(updatedBook);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(oldBook));
        when(bookMapper.toEntity(updatedRequestBookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto actualUpdatedBookDto = bookService.updateById(bookId, updatedRequestBookDto);

        assertNotNull(actualUpdatedBookDto);
        assertEquals(updatedBookDto, actualUpdatedBookDto);
        
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toEntity(updatedRequestBookDto);
        verify(bookRepository, times(1)).save(updatedBook);
        verify(bookMapper, times(1)).toDto(updatedBook);
    }

    @Test
    @DisplayName("Testing to update by invalid id")
    void updateById_NotValidId_ShouldThrowException() {
        Long bookId = 100L;
        CreateBookRequestDto createBookRequestDto =
                TestBookProvider.createDefaultBookRequestDto(TestBookProvider.createDefaultBook());

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(bookId, createBookRequestDto));
    }

    @Test
    @DisplayName("Testing to delete by valid id")
    void deleteById_ValidId_ShouldDelete() {
        Long bookId = 1L;
        Book book = TestBookProvider.createDefaultBook();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Testing to delete by invalid id")
    void deleteById_NotValidId_ShouldThrowException() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(bookId));

        verify(bookRepository, times(1)).findById(bookId);
    }
}
