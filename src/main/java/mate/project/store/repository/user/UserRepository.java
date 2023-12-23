package mate.project.store.repository.user;

import java.util.Optional;
import mate.project.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("FROM User user LEFT JOIN FETCH user.roles WHERE user.email = :email")
    Optional<User> findByEmail(String email);
}
