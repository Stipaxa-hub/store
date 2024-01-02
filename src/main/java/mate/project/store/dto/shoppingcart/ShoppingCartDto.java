package mate.project.store.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.project.store.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
