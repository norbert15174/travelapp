package pl.travel.travelapp.services.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.services.delete.interfaces.IMessageDeleteService;

@Service
public class MessageDeleteService implements IMessageDeleteService {

    private final FriendMessagesRepository friendMessagesRepository;

    @Autowired
    public MessageDeleteService(FriendMessagesRepository friendMessagesRepository) {
        this.friendMessagesRepository = friendMessagesRepository;
    }


    @Override
    public void delete(FriendMessages messages) {
        friendMessagesRepository.delete(messages);
    }

    @Override
    public void deleteById(Long messageId) {
        friendMessagesRepository.deleteById(messageId);
    }
}
