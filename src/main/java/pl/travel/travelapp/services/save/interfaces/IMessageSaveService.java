package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IMessageSaveService {
    FriendMessages save(FriendMessages messages);

    void saveAll(List<FriendMessages> messages);
}
