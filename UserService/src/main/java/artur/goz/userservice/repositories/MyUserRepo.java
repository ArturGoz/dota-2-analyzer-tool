package artur.goz.userservice.repositories;


import artur.goz.userservice.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepo extends JpaRepository<MyUser,Long> {
    Optional<MyUser> findByName(String name);
    Boolean existsByName(String name);
}
