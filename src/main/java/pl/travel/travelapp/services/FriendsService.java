package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.DTO.FriendsDTO;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.mappers.FriendsObjectMapperClass;
import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.repositories.FriendsRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <Friends> friends = friendsRepository.findFriendById(id);
        if ( friends.isPresent() ) {
            if ( friends.get().getFirstUser().getId() == id || friends.get().getSecondUser().getId() == id )
                friendsRepository.deleteById(id);
            else
                return new ResponseEntity(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity <List <FriendsDTO>> getUserFriends(Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        List <Friends> friends = friendsRepository.findFriends(user.getId());
        return getUserFriends(friends , user.getId());
    }

    @Override
    public ResponseEntity <List <FriendsDTO>> getUserFriendsById(long id) {
        List <Friends> friends = friendsRepository.findFriends(id);
        return getUserFriends(friends , id);
    }

    @Override
    public ResponseEntity <MessageDTO> sendMessage(Principal principal , long id , String message) {
        return null;
    }

    @Override
    public ResponseEntity deleteMessage(Principal principal , long messageId) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        FriendMessages message = friendMessagesRepository.findById(messageId).orElse(null);
        if ( message == null ) new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        if ( message.getSenderId() != user.getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
        friendMessagesRepository.deleteById(messageId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private ResponseEntity <List <FriendsDTO>> getUserFriends(List <Friends> friends , long id) {
        List <FriendsDTO> friendsDTOS = new ArrayList <>();
        friends.forEach(friend ->
                {
                    if ( friend.getSecondUser().getId() != id )
                        friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getSecondUser()));
                    else
                        friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getFirstUser()));
                }
        );
        return new ResponseEntity(friendsDTOS , HttpStatus.OK);
    }

}
