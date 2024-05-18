package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.cartitem.CartItemRequestDto;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toModel(CartItemRequestDto requestDto);

}
