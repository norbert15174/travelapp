package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.FriendMessages;
import pl.travel.travelapp.repositories.FriendMessagesRepository;
import pl.travel.travelapp.services.query.interfaces.IMessageQueryService;

@Service
public class MessageQueryService implements IMessageQueryService {

    private final FriendMessagesRepository friendMessagesRepository;

    @Autowired
    public MessageQueryService(FriendMessagesRepository friendMessagesRepository) {
        this.friendMessagesRepository = friendMessagesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public FriendMessages findById(Long messageId) {
        return friendMessagesRepository.findById(messageId).orElse(null);
    }
}
