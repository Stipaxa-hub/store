package mate.project.store.repository.book.spec;

import java.util.Arrays;
import mate.project.store.entity.Book;
import mate.project.store.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "authors";

    @Override
    public String getKey() {
        return KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(Arrays.stream(params)
                        .toArray());
    }
}
