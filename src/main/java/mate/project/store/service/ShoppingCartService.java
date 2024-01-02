package mate.project.store.service;

import mate.project.store.dto.cartitem.CartItemRequestDto;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.dto.cartitem.CartItemUpdateRequestDto;
import mate.project.store.dto.shoppingcart.ShoppingCartDto;
import mate.project.store.entity.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Authentication authentication);

    CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto,
                                    Authentication authentication);

    CartItemResponseDto update(Long idCartItem, CartItemUpdateRequestDto updateRequestDto);

    void delete(Long idCartItem);

    void createShoppingCart(User user);
}
