package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.order.OrderDto;
import mate.project.store.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    @AfterMapping
    default void setUserId(@MappingTarget OrderDto orderDto, Order order) {
        orderDto.setUserId(order.getUser().getId());
    }
}
