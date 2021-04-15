package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.models.Friends;

import java.security.Principal;
import java.util.List;

public interface FriendsInterface {

    //Creating group of friends
    ResponseEntity<Friends> linkUsersAsFriendsGroup(List <Float> id);

    //Delete friend
    ResponseEntity deleteFriend(Principal principal, long id);
    //Delete group
    ResponseEntity deleteGroup(Principal principal, long groupId);
    //Delete user from group
    ResponseEntity<Friends> deleteUserFromGroup(Principal principal, long groupId, long userId);
    //Add new user to group
    ResponseEntity<Friends> addUserToGroup(Principal principal, long id);
    //Change groups' leader
    ResponseEntity<Friends> changeLeader(Principal principal,long id);
    //Leave user from group
    ResponseEntity<Friends> leaveGroup(Principal principal, long groupId);
}
