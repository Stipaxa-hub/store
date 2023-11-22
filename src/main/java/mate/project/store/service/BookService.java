package mate.project.store.service;

import java.util.List;
import mate.project.store.entity.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
