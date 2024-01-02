package mate.project.store.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "Name can't be empty")
        String name,
        String description
) {
}
