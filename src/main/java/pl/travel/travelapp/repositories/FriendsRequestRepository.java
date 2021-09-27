package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.FriendsRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRequestRepository extends JpaRepository <FriendsRequest, Long> {
    @Query("select f from FriendsRequest f left join fetch f.sender s where (f.receiver = :receiverID and s.id = :senderID) or (f.receiver = :senderID and s.id = :receiverID)")
    List <FriendsRequest> findFirstByReceiver(Long receiverID , Long senderID);

    @Query("select f from FriendsRequest f left join fetch f.sender where f.receiver = :id and f.isFriends = false")
    Optional <List <FriendsRequest>> findAllUserInvitations(Long id);

    @Query("select f from FriendsRequest f left join fetch f.sender s where s.id = :id and f.isFriends = false")
    Optional <List <FriendsRequest>> findUserRequestSent(Long id);

    @Query("select f from FriendsRequest f left join fetch f.sender where f.id = :id")
    Optional <FriendsRequest> findFriendsRequest(Long id);

}
