package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.FriendMessages;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.services.save.interfaces.IMessageSaveService;

import java.util.List;

@Service
public class MessageSaveService implements IMessageSaveService {

    private final FriendMessagesRepository friendMessagesRepository;

    @Autowired
    public MessageSaveService(FriendMessagesRepository friendMessagesRepository) {
        this.friendMessagesRepository = friendMessagesRepository;
    }

    @Transactional
    @Override
    public FriendMessages save(FriendMessages messages) {
        return friendMessagesRepository.save(messages);
    }

    @Transactional
    @Override
    public void saveAll(List <FriendMessages> messages) {
        friendMessagesRepository.saveAll(messages);
    }


}
