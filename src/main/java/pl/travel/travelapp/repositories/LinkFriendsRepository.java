package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.LinkFriends;

public interface LinkFriendsRepository extends JpaRepository<LinkFriends, Long> {
}
