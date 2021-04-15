package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.repositories.LinkFriendsRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

import java.security.Principal;
import java.util.List;

@Service
public class FriendsService implements FriendsInterface, FriendsMessageInterface {

    private PersonalDataRepository personalDataRepository;
    private LinkFriendsRepository linkFriendsRepository;
    private FriendsRepository friendsRepository;
    private FriendMessagesRepository friendMessagesRepository;
    @Autowired
    public FriendsService(PersonalDataRepository personalDataRepository, LinkFriendsRepository linkFriendsRepository, FriendsRepository friendsRepository, FriendMessagesRepository friendMessagesRepository) {
        this.personalDataRepository = personalDataRepository;
        this.linkFriendsRepository = linkFriendsRepository;
        this.friendsRepository = friendsRepository;
        this.friendMessagesRepository = friendMessagesRepository;
    }


    @Override
    public ResponseEntity <Friends> linkUsersAsFriendsGroup(List <Float> id) {
        return null;
    }



    @Override
    public ResponseEntity deleteFriend(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity deleteGroup(Principal principal , long groupId) {
        return null;
    }

    @Override
    public ResponseEntity <Friends> deleteUserFromGroup(Principal principal , long groupId , long userId) {
        return null;
    }

    @Override
    public ResponseEntity <Friends> addUserToGroup(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity <Friends> changeLeader(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity <Friends> leaveGroup(Principal principal , long groupId) {
        return null;
    }
}
