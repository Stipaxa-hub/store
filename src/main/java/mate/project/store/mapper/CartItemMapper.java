package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

}
