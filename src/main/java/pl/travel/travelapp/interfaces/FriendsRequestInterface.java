package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.models.FriendsRequest;

import java.security.Principal;
import java.util.List;

public interface FriendsRequestInterface {

    ResponseEntity<FriendsRequest> addUserToFriendsWaitingList(Principal principal,long id);
    ResponseEntity deleteRequest(Principal principal,long id);
    ResponseEntity<List<UserFriendRequestDTO>> findRequestById(Principal principal);

}
