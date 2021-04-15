package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.models.FriendsRequest;

import java.security.Principal;
import java.util.List;

public interface FriendsRequestInterface {

    //Add user to waiting list and wait for accept
    ResponseEntity<FriendsRequest> addUserToFriendsWaitingList(Principal principal,long id);
    //Receiver does not accept the request
    ResponseEntity deleteRequest(Principal principal,long id);
    //Finding all user friend request
    ResponseEntity<List<UserFriendRequestDTO>> findRequestsByPrincipal(Principal principal);
    //Checking if request is correct
    boolean checkIfRequestIsCorrect(Principal principal, long id);


}
