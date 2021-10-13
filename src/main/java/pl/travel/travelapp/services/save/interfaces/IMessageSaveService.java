package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;

import java.time.LocalDateTime;
import java.util.Set;

public interface IMessageSaveService {
    FriendMessages save(FriendMessages messages);

    Set <MessageDTO> getMessagesAfter(LocalDateTime date , Long friendsId);
}
