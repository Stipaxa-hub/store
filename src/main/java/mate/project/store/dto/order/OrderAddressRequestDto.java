package mate.project.store.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderAddressRequestDto {
    @NotBlank
    private String shippingAddress;
}
