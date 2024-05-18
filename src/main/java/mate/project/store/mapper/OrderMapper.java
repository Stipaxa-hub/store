package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.order.OrderDto;
import mate.project.store.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);
}
