package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String s);
    @Query("select u from User u where u.login = :s or u.email = :mail" )
    Optional<User> checkIfExist(String s, String mail);
    Optional<User> findFirstByLogin(String s);
    Optional<User> findFirstByEmail(String mail);
    @Query("select u from User u left join fetch u.personalData p left join fetch p.personalDescription where u.login = :username")
    User findPersonalDataByUser(String username);
}
