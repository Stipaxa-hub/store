package mate.project.store.controller;

import jakarta.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.order.OrderAddressRequestDto;
import mate.project.store.dto.order.OrderDto;
import mate.project.store.dto.order.OrderStatusRequestDto;
import mate.project.store.dto.orderitem.OrderItemDto;
import mate.project.store.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto placeOrder(@RequestBody @Valid OrderAddressRequestDto requestDto,
                               Authentication authentication) {
        return orderService.placeOrder(requestDto, authentication);
    }

    @GetMapping
    public List<OrderDto> getAllOrders(Authentication authentication,
                                       Pageable pageable) {
        return orderService.getAllOrders(authentication, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{orderId}")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                      @RequestBody @Valid OrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    @GetMapping("{orderId}/items")
    public Set<OrderItemDto> getAllOrderItems(@PathVariable Long orderId,
                                              Authentication authentication) {
        return orderService.getAllOrderItems(orderId, authentication);
    }

    @GetMapping("{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long orderItemId,
                                         Authentication authentication) {
        return orderService.getOrderItemById(orderId, orderItemId, authentication);
    }
}
