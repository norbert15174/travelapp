package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.FriendsDTO;

import java.security.Principal;
import java.util.List;

public interface FriendsInterface {

    ResponseEntity deleteFromFriendList(Principal principal , long id);

    ResponseEntity <List <FriendsDTO>> getUserFriends(Principal principal);

    ResponseEntity <List <FriendsDTO>> getUserFriendsById(long id);
}
