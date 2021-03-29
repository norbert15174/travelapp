package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.repositories.LinkFriendsRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

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





}
