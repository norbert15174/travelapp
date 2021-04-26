package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.FriendsRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRequestRepository extends JpaRepository<FriendsRequest,Long> {
    @Query("select f from FriendsRequest f left join fetch f.sender where f.receiver = :receiverID and f.sender.id = :senderID")
    List<FriendsRequest> findFirstByReceiver(long receiverID,long senderID);
    @Query("select f from FriendsRequest f left join fetch f.sender where f.receiver = :id and f.isFriends = false")
    Optional<List<FriendsRequest>> findAllUserInvitations(long id);
    @Query("select f from FriendsRequest f left join fetch f.sender where f.id = :id")
    Optional<FriendsRequest> findFriendsRequest(long id);

}
