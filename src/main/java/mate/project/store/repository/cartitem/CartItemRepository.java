package mate.project.store.repository.cartitem;

import java.util.Optional;
import java.util.Set;
import mate.project.store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long>,
        JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findCartItemById(Long id);

    Set<CartItem> getCartItemsByShoppingCartId(Long id);

    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.isDeleted = true "
            + "WHERE item.shoppingCart.id = :shoppingCartId")
    void deleteCartItemsByShoppingCartId(Long shoppingCartId);
}
