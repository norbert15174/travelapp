package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.models.FriendMessages;
import pl.travel.travelapp.models.Friends;

public interface IMessageDeleteService {

    void delete(FriendMessages messages);

    void deleteById(Long messageId);

}
