package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.FriendsRequest;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query("select f from Friends f left join fetch f.firstUser left join fetch f.secondUser where f.firstUser.id = :id or f.secondUser.id = :id")
        List <Friends> findFriends(long id);
}
