package mate.project.store.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.BookDto;
import mate.project.store.dto.BookSearchParametersDto;
import mate.project.store.dto.CreateBookRequestDto;
import mate.project.store.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @PostMapping
    public BookDto save(@RequestBody CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id, @RequestBody CreateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParametersDto bookSearchParametersDto) {
        return bookService.search(bookSearchParametersDto);
    }
}
