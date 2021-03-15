package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.Friends;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
}
