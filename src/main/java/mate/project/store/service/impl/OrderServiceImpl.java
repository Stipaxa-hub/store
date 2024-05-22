package mate.project.store.service.impl;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.order.OrderAddressRequestDto;
import mate.project.store.dto.order.OrderDto;
import mate.project.store.dto.order.OrderStatusRequestDto;
import mate.project.store.dto.orderitem.OrderItemDto;
import mate.project.store.entity.CartItem;
import mate.project.store.entity.Order;
import mate.project.store.entity.OrderItem;
import mate.project.store.entity.ShoppingCart;
import mate.project.store.entity.User;
import mate.project.store.entity.enums.Status;
import mate.project.store.exception.EntityNotFoundException;
import mate.project.store.mapper.OrderItemMapper;
import mate.project.store.mapper.OrderMapper;
import mate.project.store.repository.cartitem.CartItemRepository;
import mate.project.store.repository.order.OrderRepository;
import mate.project.store.repository.shoppingcart.ShoppingCartRepository;
import mate.project.store.repository.user.UserRepository;
import mate.project.store.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto placeOrder(OrderAddressRequestDto requestDto, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with email: "
                        + authentication.getName()));
        Order order = buildOrder(user, requestDto.getShippingAddress());
        order = orderRepository.save(order);
        clearCartByShoppingCartId(user.getId());
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getAllOrders(Authentication authentication, Pageable pageable) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with email: "
                        + authentication.getName()));
        List<Order> orders = orderRepository.getOrdersByUserId(user.getId());
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto) {
        Order order = orderRepository.getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + orderId));
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<OrderItemDto> getAllOrderItems(Long orderId, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with email: "
                        + authentication.getName()));
        return orderRepository.getOrdersByUserId(user.getId()).stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + orderId))
                .getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderItemDto getOrderItemById(Long orderId,
                                         Long orderItemId,
                                         Authentication authentication) {
        return getAllOrderItems(orderId, authentication).stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item with id: "
                        + orderId));
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order buildOrder(User user, String address) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.CREATED);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
        List<OrderItem> orderItems = getOrdersItems(user.getId());
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setTotal(calculateTotalPrice(orderItems));

        Set<OrderItem> orderItemSet = new TreeSet<>(
                (item1, item2) -> {
                    int quantityComparison = Integer.compare(item1.getQuantity(),
                            item2.getQuantity());
                    if (quantityComparison == 0) {
                        return item1.getPrice().compareTo(item2.getPrice());
                    }
                    return quantityComparison;
                }
        );
        orderItemSet.addAll(orderItems);
        order.setOrderItems(orderItemSet);

        return order;
    }

    private List<OrderItem> getOrdersItems(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(id);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        return cartItems.stream()
                .map(orderItemMapper::cartItemToOrderItem)
                .collect(Collectors.toList());
    }

    private void clearCartByShoppingCartId(Long id) {
        cartItemRepository.deleteCartItemsByShoppingCartId(id);
    }
}
