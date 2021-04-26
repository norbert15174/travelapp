package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.DTO.FriendsDTO;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.mappers.FriendsObjectMapperClass;
import pl.travel.travelapp.mappers.PersonalDataObjectMapperClass;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.repositories.FriendsRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
public class FriendsService implements FriendsInterface, FriendsMessageInterface {

    private FriendsRepository friendsRepository;
    private FriendMessagesRepository friendMessagesRepository;
    private PersonalService personalService;
    @Autowired
    public FriendsService(FriendsRepository friendsRepository , FriendMessagesRepository friendMessagesRepository , PersonalService personalService) {
        this.friendsRepository = friendsRepository;
        this.friendMessagesRepository = friendMessagesRepository;
        this.personalService = personalService;
    }

    @Override
    public ResponseEntity deleteFromFriendList(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity getUserFriends(Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        List <Friends> friends = friendsRepository.findFriends(user.getId());
        List<FriendsDTO> friendsDTOS = new ArrayList <>();
        friends.forEach(friend ->
                {
                    if(friend.getSecondUser().getId() != user.getId())
                        friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getSecondUser()));
                    else
                        friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getFirstUser()));
                }
        );
        return new ResponseEntity(friendsDTOS,HttpStatus.OK);
    }

    @Override
    public ResponseEntity <MessageDTO> sendMessage(Principal principal , long id , String message) {
        return null;
    }

    @Override
    public ResponseEntity deleteMessage(Principal principal , long messageId) {
        return null;
    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void test(){
//        System.out.println(friendsRepository.findFriends(2L).get(0).getSecondUser().getId());
//        //System.out.println(friendsRepository.findAll());
//    }
}
