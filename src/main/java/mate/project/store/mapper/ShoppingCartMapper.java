package mate.project.store.mapper;

import mate.project.store.config.MapperConfig;
import mate.project.store.dto.shoppingcart.ShoppingCartDto;
import mate.project.store.entity.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartDto shoppingCartDto,
                           ShoppingCart shoppingCart) {
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
    }
}
