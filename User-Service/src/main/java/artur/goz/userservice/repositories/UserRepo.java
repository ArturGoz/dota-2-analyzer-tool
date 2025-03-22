package artur.goz.userservice.repositories;


import artur.goz.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByName(String name);
    Boolean existsByName(String name);
}
