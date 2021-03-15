package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.travel.travelapp.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String s);
    @Query("select u from User u where u.login = :s or u.email = :mail" )
    Optional<User> checkIfExist(String s, String mail);
}
