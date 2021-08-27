package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.entites.Friends;

public interface IFriendsDeleteService {

    void delete(Friends friend);

    void deleteById(Long friendId);

}
