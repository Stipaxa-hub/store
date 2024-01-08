package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.orderitem.OrderItemDto;
import mate.project.store.entity.CartItem;
import mate.project.store.entity.Order;
import mate.project.store.entity.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem, Order order);

    @AfterMapping
    default void initializeOrderInItem(@MappingTarget OrderItem orderItem,
                                       CartItem cartItem, Order order) {
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setPrice(cartItem.getBook().getPrice());
    }

    @AfterMapping
    default void setBookId(@MappingTarget OrderItemDto orderItemDto,
                           OrderItem orderItem) {
        orderItemDto.setBookId(orderItem.getBook().getId());
    }
}
