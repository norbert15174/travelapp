package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.entites.FriendMessages;

public interface IMessageDeleteService {

    void delete(FriendMessages messages);

    void deleteById(Long messageId);

}
