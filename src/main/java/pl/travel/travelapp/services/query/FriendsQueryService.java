package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.services.query.interfaces.IFriendsQueryService;

import java.util.List;
import java.util.Optional;

@Service
public class FriendsQueryService implements IFriendsQueryService {
    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsQueryService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List <Friends> findFriendsByUserId(Long id) {
        return friendsRepository.findFriends(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List <Friends> findByFriendId(Long id) {
        return friendsRepository.findFriends(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <Friends> findFriendByFirstAndSecond(Long firstId , Long secondId) {
        return friendsRepository.findFriendByFirstAndSecond(firstId , secondId);
    }


}
