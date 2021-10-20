package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IMessageQueryService {

    FriendMessages findById(Long messageId);

    List <MessageDTO> getMessages(Long userId , Long friendId , Integer page);

    Set <FriendMessages> getUserNewMessages(Long userId , Long friendId);

    Set <MessageDTO> getMessagesAfter(LocalDateTime date , Long friendsId);
}
