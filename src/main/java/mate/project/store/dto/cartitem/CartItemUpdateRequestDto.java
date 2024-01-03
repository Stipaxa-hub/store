package mate.project.store.dto.cartitem;

import jakarta.validation.constraints.Min;

public record CartItemUpdateRequestDto(
        @Min(1)
        int quantity
) {
}
