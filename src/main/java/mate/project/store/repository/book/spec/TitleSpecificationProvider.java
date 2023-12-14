package mate.project.store.repository.book.spec;

import java.util.Arrays;
import mate.project.store.entity.Book;
import mate.project.store.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "titles";

    @Override
    public String getKey() {
        return KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("title")
                .in(Arrays.stream(params)
                        .toArray());
    }
}
