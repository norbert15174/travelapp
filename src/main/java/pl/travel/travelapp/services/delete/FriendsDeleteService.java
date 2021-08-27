package pl.travel.travelapp.services.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.entites.Friends;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.services.delete.interfaces.IFriendsDeleteService;

@Service
public class FriendsDeleteService implements IFriendsDeleteService {

    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsDeleteService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }


    @Override
    public void delete(Friends friend) {
        friendsRepository.delete(friend);
    }

    @Override
    public void deleteById(Long friendId) {
        friendsRepository.deleteById(friendId);
    }
}
