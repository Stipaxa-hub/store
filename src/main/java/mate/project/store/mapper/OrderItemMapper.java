package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.orderitem.OrderItemDto;
import mate.project.store.entity.CartItem;
import mate.project.store.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem cartItemToOrderItem(CartItem cartItem);
}
