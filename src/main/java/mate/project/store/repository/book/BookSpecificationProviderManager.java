package mate.project.store.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.store.entity.Book;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.repository.SpecificationProvider;
import mate.project.store.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(bookSpecificationProvider -> bookSpecificationProvider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find correct specification provider for key "
                                + key));
    }
}
