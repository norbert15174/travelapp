package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.FriendsDTO;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;
import pl.travel.travelapp.entites.Friends;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.SharedAlbum;
import pl.travel.travelapp.interfaces.FriendsInterface;
import pl.travel.travelapp.interfaces.FriendsMessageInterface;
import pl.travel.travelapp.mappers.FriendsObjectMapperClass;
import pl.travel.travelapp.services.delete.interfaces.IFriendsDeleteService;
import pl.travel.travelapp.services.delete.interfaces.IMessageDeleteService;
import pl.travel.travelapp.services.query.interfaces.IFriendsQueryService;
import pl.travel.travelapp.services.query.interfaces.IMessageQueryService;
import pl.travel.travelapp.services.save.interfaces.IMessageSaveService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FriendsService implements FriendsInterface, FriendsMessageInterface {

    private final PersonalService personalService;
    private final IndividualAlbumService individualAlbumService;
    private final IFriendsQueryService friendsQueryService;
    private final IFriendsDeleteService friendsDeleteService;
    private final IMessageDeleteService messageDeleteService;
    private final IMessageQueryService messageQueryService;
    private final IMessageSaveService messageSaveService;

    @Autowired
    public FriendsService(PersonalService personalService , IndividualAlbumService individualAlbumService , IFriendsQueryService friendsQueryService , IFriendsDeleteService friendsDeleteService , IMessageDeleteService messageDeleteService , IMessageQueryService messageQueryService , IMessageSaveService messageSaveService) {
        this.personalService = personalService;
        this.individualAlbumService = individualAlbumService;
        this.friendsQueryService = friendsQueryService;
        this.friendsDeleteService = friendsDeleteService;
        this.messageDeleteService = messageDeleteService;
        this.messageQueryService = messageQueryService;
        this.messageSaveService = messageSaveService;
    }

    @Override
    @Transactional
    public ResponseEntity deleteFromFriendList(Principal principal , long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <Friends> friendsOpt = friendsQueryService.findFriendByFirstAndSecond(user.getId() , id);
        if ( friendsOpt.isPresent() ) {
            Friends friend = friendsOpt.get();
            Long friendToDeleteId = friend.getFirstUser().getId() == user.getId() ? friend.getSecondUser().getId() : friend.getFirstUser().getId();
            List <Long> sharedAlbumIds = individualAlbumService.findAllUserSharedAlbumsByOwnerAndSharedUserId(id , user.getId()).stream().map(SharedAlbum::getId).collect(Collectors.toList());
            individualAlbumService.deleteSharedUserDuringFriendDelete(sharedAlbumIds);
            friendsDeleteService.deleteById(friend.getId());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity <List <FriendsDTO>> getUserFriends(Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        List <Friends> friends = friendsQueryService.findFriendsByUserId(user.getId());
        Long friendsId = !friends.isEmpty() ? friends.get(0).getId() : -1;
        return new ResponseEntity <>(getUserFriends(friends , user.getId() , friendsId) , HttpStatus.OK);
    }

    @Override
    public ResponseEntity <List <FriendsDTO>> getUserFriendsById(long id) {
        List <Friends> friends = friendsQueryService.findFriendsByUserId(id);
        List <FriendsDTO> friendsDTOS = getUserFriends(friends , id);
        return new ResponseEntity <>(friendsDTOS , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <MessageDTO> sendMessage(Principal principal , long id , MessageDTO messageDTO) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <Friends> friend = friendsQueryService.findFriendsByUserIdAndFriendId(user.getId() , id);
        if ( friend.isPresent() ) {
            Friends friendToSave = friend.get();
            FriendMessages message = new FriendMessages(user , friendToSave , messageDTO);
            return new ResponseEntity <>(new MessageDTO(messageSaveService.save(message)) , HttpStatus.CREATED);
        }
        return new ResponseEntity <>(HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity deleteMessage(Principal principal , long messageId) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        FriendMessages message = messageQueryService.findById(messageId);
        if ( message == null ) new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        if ( message.getSender().getId() != user.getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
        messageDeleteService.deleteById(messageId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private List <FriendsDTO> getUserFriends(List <Friends> friends , long id) {
        List <FriendsDTO> friendsDTOS = new ArrayList <>();
        for (Friends friend : friends) {
            if ( friend.getSecondUser().getId() != id )
                friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getSecondUser()));
            else
                friendsDTOS.add(FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getFirstUser()));
        }
        return friendsDTOS;
    }

    private List <FriendsDTO> getUserFriends(List <Friends> friends , long id , Long friendId) {
        List <FriendsDTO> friendsDTOS = new ArrayList <>();
        for (Friends friend : friends) {
            if ( friend.getSecondUser().getId() != id ) {
                FriendsDTO friendsDTO = FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getSecondUser());
                friendsDTO.setFriendId(friendId);
                friendsDTOS.add(friendsDTO);
            } else {
                FriendsDTO friendsDTO = FriendsObjectMapperClass.mapPersonalDataToFriendsDTO(friend.getFirstUser());
                friendsDTO.setFriendId(friendId);
                friendsDTOS.add(friendsDTO);
            }

        }
        return friendsDTOS;
    }

    @Transactional
    @Override
    public ResponseEntity <List <MessageDTO>> getMessages(Principal principal , Long id , Integer page) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <Friends> friend = friendsQueryService.findFriendsByUserIdAndFriendId(user.getId() , id);
        if ( !friend.isPresent() ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity <>(messageQueryService.getMessages(user.getId() , id , page) , HttpStatus.OK);
    }
}
