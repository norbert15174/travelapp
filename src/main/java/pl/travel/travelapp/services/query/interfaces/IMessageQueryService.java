package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.models.FriendMessages;

public interface IMessageQueryService {

    FriendMessages findById(Long messageId);

}
