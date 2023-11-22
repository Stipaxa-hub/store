package mate.project.store;

import java.math.BigDecimal;
import mate.project.store.entity.Book;
import mate.project.store.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("book");
            book.setAuthor("author");
            book.setIsbn("isbn");
            book.setPrice(BigDecimal.valueOf(100));
            book.setDescription("book description");
            book.setCoverImage("gold");

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
