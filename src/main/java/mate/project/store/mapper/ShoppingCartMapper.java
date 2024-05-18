package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.shoppingcart.ShoppingCartDto;
import mate.project.store.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "userId", target = "user.id")
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

}
