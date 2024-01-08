package mate.project.store.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.project.store.entity.enums.Status;

@Data
public class OrderStatusRequestDto {
    @NotNull
    private Status status;
}
