package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.FriendMessages;

public interface FriendMessagesRepository extends JpaRepository<FriendMessages, Long> {
}
