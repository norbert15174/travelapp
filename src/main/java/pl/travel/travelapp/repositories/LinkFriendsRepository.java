package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.LinkFriends;

@Repository
public interface LinkFriendsRepository extends JpaRepository<LinkFriends, Long> {
}
