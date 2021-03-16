package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.Friends;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
}
