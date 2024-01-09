package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.entity.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    default void initialize(@MappingTarget CartItemResponseDto responseDto, CartItem cartItem) {
        responseDto.setId(cartItem.getId());
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
    }
}
