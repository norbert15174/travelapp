package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.builders.FriendsBuilder;
import pl.travel.travelapp.builders.LinkFriendsBuilder;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.LinkFriends;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.repositories.LinkFriendsRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendsService implements FriendsInterface, FriendsMessageInterface {

    private PersonalDataRepository personalDataRepository;
    private LinkFriendsRepository linkFriendsRepository;
    private FriendsRepository friendsRepository;
    private FriendMessagesRepository friendMessagesRepository;
    private PersonalService personalService;
    @Autowired
    public FriendsService(PersonalDataRepository personalDataRepository, LinkFriendsRepository linkFriendsRepository, FriendsRepository friendsRepository, FriendMessagesRepository friendMessagesRepository,PersonalService personalService) {
        this.personalDataRepository = personalDataRepository;
        this.linkFriendsRepository = linkFriendsRepository;
        this.friendsRepository = friendsRepository;
        this.friendMessagesRepository = friendMessagesRepository;
        this.personalService = personalService;
    }


    @Override
    @Transactional
    public ResponseEntity <Friends> linkUsersAsFriendsGroup(List <Long> id, Principal principal) {
        //REMEMBER TO CREATE FRIENDS LIST DTO WHICH SHOULD BE RETURNED BY THIS METHOD
        if(id.size() > 10) return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        PersonalData leader = personalService.getPersonalInformation(principal.getName());
        if(leader.getId() != id.get(0)) return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        Friends linkedFriends = new FriendsBuilder().setMessages(new FriendMessages()).setGroupLeader(id.get(0)).createFriends();
        friendsRepository.save(linkedFriends);
        Friends group = new FriendsBuilder()
                .setGroupLeader(id.get(0))
                .setMessages(new FriendMessages())
                .createFriends();
        List<LinkFriends> linkFriends = new ArrayList <>();
        List<PersonalData> users = new ArrayList <>();

        id.forEach((userId) ->
            users.add(personalDataRepository.findById(userId).get())
        );
        users.forEach(user ->
            linkFriends.add(new LinkFriendsBuilder().setUser(user).setFriend(group).createLinkFriends())
        );

        for(int i = 0; i<users.size();i++){
            users.get(i).addFriend(linkFriends.get(i));
            personalDataRepository.save(users.get(i));
            linkFriendsRepository.save(linkFriends.get(i));
        }



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
