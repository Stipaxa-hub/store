package mate.project.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.cartitem.CartItemRequestDto;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.dto.cartitem.CartItemUpdateRequestDto;
import mate.project.store.dto.shoppingcart.ShoppingCartDto;
import mate.project.store.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get all shopping cart")
    public ShoppingCartDto getAll(Authentication authentication) {
        return shoppingCartService.getShoppingCart(authentication);
    }

    @PostMapping
    @Operation(summary = "Add cart item into shopping cart")
    public CartItemResponseDto addCartItem(@RequestBody @Valid CartItemRequestDto itemRequestDto,
                                           Authentication authentication) {
        return shoppingCartService.addCartItem(itemRequestDto, authentication);
    }

    @PutMapping("/cart-items/{idCartItem}")
    @Operation(summary = "Update cart item into shopping car")
    public CartItemResponseDto update(@PathVariable Long idCartItem,
                                      @RequestBody @Valid CartItemUpdateRequestDto updateRequest) {
        return shoppingCartService.update(idCartItem, updateRequest);
    }

    @DeleteMapping("/cart-items-{idCartItem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item from shopping cart")
    public void delete(@PathVariable Long idCartItem) {
        shoppingCartService.delete(idCartItem);
    }
}
