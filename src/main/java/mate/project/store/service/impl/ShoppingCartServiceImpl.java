package mate.project.store.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.cartitem.CartItemRequestDto;
import mate.project.store.dto.cartitem.CartItemResponseDto;
import mate.project.store.dto.cartitem.CartItemUpdateRequestDto;
import mate.project.store.dto.shoppingcart.ShoppingCartDto;
import mate.project.store.entity.Book;
import mate.project.store.entity.CartItem;
import mate.project.store.entity.ShoppingCart;
import mate.project.store.entity.User;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.CartItemMapper;
import mate.project.store.mapper.ShoppingCartMapper;
import mate.project.store.repository.book.BookRepository;
import mate.project.store.repository.cartitem.CartItemRepository;
import mate.project.store.repository.shoppingcart.ShoppingCartRepository;
import mate.project.store.repository.user.UserRepository;
import mate.project.store.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional(readOnly = true)
    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user with email: " + email));
        ShoppingCart shoppingCart = getCurrentUserCart(currentUser);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }

    @Transactional
    @Override
    public CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto,
                                           Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user with email:" + email));
        ShoppingCart shoppingCart = getCurrentUserCart(currentUser);
        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find book with id: "
                                + cartItemRequestDto.bookId()));
        CartItem cartItem = getCartItem(shoppingCart, book);
        cartItem.setQuantity(cartItemRequestDto.quantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemResponseDto update(Long idCartItem,
                                      CartItemUpdateRequestDto updateRequestDto) {
        CartItem cartItem = cartItemRepository.findCartItemById(idCartItem)
                .orElseThrow(() ->
                        new EntityNotFoundException("can't find item with id: " + idCartItem));
        cartItem.setId(idCartItem);
        cartItem.setQuantity(updateRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long idCartItem) {
        cartItemRepository.deleteById(idCartItem);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, Book book) {
        Optional<CartItem> cartItemOptional = shoppingCart.getCartItems().stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();
        CartItem cartItem;
        if (cartItemOptional.isPresent()) {
            cartItem = cartItemOptional.get();
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
        }
        return cartItem;
    }

    private ShoppingCart getCurrentUserCart(User currentUser) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(
                currentUser.getId()
        );
        shoppingCart.setCartItems(
                cartItemRepository.getCartItemsByShoppingCartId(shoppingCart.getId())
        );
        return shoppingCart;
    }
}
