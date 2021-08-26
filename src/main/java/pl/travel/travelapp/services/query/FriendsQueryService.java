package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.repositories.FriendsRepository;

import java.util.List;
import java.util.Set;

@Service
public class FriendsQueryService {
    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsQueryService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }


    public List <Friends> findFriendsByUserId(long id){
        return friendsRepository.findFriends(id);
    }

}
