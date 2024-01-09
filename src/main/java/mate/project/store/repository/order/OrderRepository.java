package mate.project.store.repository.order;

import java.util.List;
import java.util.Optional;
import mate.project.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
    List<Order> getOrdersByUserId(Long userId);

    Optional<Order> getOrderById(Long orderId);
}
