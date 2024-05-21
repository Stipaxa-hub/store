package mate.project.store.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.project.store.dto.book.BookDto;
import mate.project.store.dto.book.CreateBookRequestDto;
import mate.project.store.entity.Book;
import mate.project.store.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class TestBookProvider {

    public static Book createDefaultBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title For Test");
        book.setAuthor("Book Author For Test");
        book.setIsbn("0-061-96436-0");
        book.setPrice(BigDecimal.valueOf(100));
        book.setDescription("Book Description For Test");
        book.setCoverImage("Book Cover Image For Test");
        book.setCategories(Set.of(TestCategoryProvider.createDefaultCategory()));
        return book;
    }

    public static List<Book> createDefaultListBook() {
        List<Book> books = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Book book = new Book();
            book.setId(Long.valueOf(i));
            book.setTitle("Book" + i);
            book.setAuthor("Author" + i);
            book.setIsbn("1234567" + i);
            book.setPrice(BigDecimal.valueOf(i));
            book.setDescription("Description" + i);
            book.setCoverImage("CoverImage" + i);
            book.setCategories(Set.of(TestCategoryProvider.createDefaultCategory()));
            books.add(book);
        }
        return books;
    }

    public static List<BookDto> createDefaultListBookDto() {
        List<BookDto> books = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            BookDto bookDto = BookDto.builder()
                    .id(Long.valueOf(i))
                    .title("Book" + i)
                    .author("Author" + i)
                    .isbn("1234567" + i)
                    .price(BigDecimal.valueOf(i))
                    .description("Description" + i)
                    .coverImage("CoverImage" + i)
                    .categoriesIds(Set.of(Long.valueOf(i)))
                    .build();
            books.add(bookDto);
        }

        return books;
    }

    public static BookDto createDefaultBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .categoriesIds(book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static CreateBookRequestDto createDefaultBookRequestDto(Book book) {
        return CreateBookRequestDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .categoriesIds(book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
