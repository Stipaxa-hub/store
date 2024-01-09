package mate.project.store.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "Title can't be null")
    @NotBlank(message = "Title can't be empty")
    @Size(max = 200, message = "Title should not be greater than 200")
    private String title;
    @NotNull(message = "Author can't be null")
    @NotBlank(message = "Author can't be empty")
    @Size(max = 200, message = "Author should not be greater than 200")
    private String author;
    @NotNull(message = "ISBN can't be null")
    @NotBlank(message = "ISBN can't be empty")
    @ISBN(message = "ISBN should be correct")
    private String isbn;
    @NotNull(message = "Price can't be null")
    @Min(value = 1, message = "Price should be greater than 0")
    private BigDecimal price;
    @NotNull(message = "Description can't be null")
    @NotBlank(message = "Description can't be empty")
    private String description;
    private String coverImage;
    @NotNull
    private Set<Long> categoriesIds;
}
