package mate.project.store.service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;
import mate.project.store.dto.order.OrderAddressRequestDto;
import mate.project.store.dto.order.OrderDto;
import mate.project.store.dto.order.OrderStatusRequestDto;
import mate.project.store.dto.orderitem.OrderItemDto;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto placeOrder(OrderAddressRequestDto requestDto,
                        Authentication authentication);

    List<OrderDto> getAllOrders(Authentication authentication, Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto);

    Set<OrderItemDto> getAllOrderItems(Long orderId, Authentication authentication);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, Authentication authentication);
}
