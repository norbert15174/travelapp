package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.entites.Friends;

import java.util.List;
import java.util.Optional;

public interface IFriendsQueryService {

    List <Friends> findFriendsByUserId(Long id);

    List <Friends> findByFriendId(Long id);

    Optional <Friends> findFriendByFirstAndSecond(Long firstId , Long secondId);

    Optional <Friends> findFriendsByUserIdAndFriendId(Long userId, Long friendId);

}
