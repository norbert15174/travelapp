package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.FriendMessages;

@Repository
public interface FriendMessagesRepository extends JpaRepository<FriendMessages, Long> {
}
