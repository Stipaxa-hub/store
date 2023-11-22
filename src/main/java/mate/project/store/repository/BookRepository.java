package mate.project.store.repository;

import java.util.List;
import mate.project.store.entity.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
