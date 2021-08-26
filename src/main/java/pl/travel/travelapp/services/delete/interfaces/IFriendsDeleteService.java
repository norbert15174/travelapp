package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.models.Friends;

public interface IFriendsDeleteService {

    void delete(Friends friend);

    void deleteById(Long friendId);

}
