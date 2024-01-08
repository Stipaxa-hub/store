package mate.project.store.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mate.project.store.dto.orderitem.OrderItemDto;
import mate.project.store.entity.enums.Status;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderItemDto> orderItems;
    private LocalDateTime orderDateTime;
    private BigDecimal total;
    private Status status;
}
