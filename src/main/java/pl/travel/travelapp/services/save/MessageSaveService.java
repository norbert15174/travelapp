package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.services.save.interfaces.IMessageSaveService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class MessageSaveService implements IMessageSaveService {

    private final FriendMessagesRepository friendMessagesRepository;

    @Autowired
    public MessageSaveService(FriendMessagesRepository friendMessagesRepository) {
        this.friendMessagesRepository = friendMessagesRepository;
    }

    @Override
    public FriendMessages save(FriendMessages messages) {
        return friendMessagesRepository.save(messages);
    }

    @Override
    public Set <MessageDTO> getMessagesAfter(LocalDateTime date , Long friendsId) {
        return friendMessagesRepository.findMessagesAfter(date == null ? LocalDateTime.now().minusHours(1) : date, friendsId);
    }
}
