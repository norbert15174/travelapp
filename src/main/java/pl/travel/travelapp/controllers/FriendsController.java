package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.travel.travelapp.DTO.FriendsDTO;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.services.FriendsRequestService;
import pl.travel.travelapp.services.FriendsService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = "*")
public class FriendsController {


    private FriendsRequestService friendsRequestService;
    private FriendsService friendsService;

    public FriendsController(FriendsRequestService friendsRequestService , FriendsService friendsService) {
        this.friendsRequestService = friendsRequestService;
        this.friendsService = friendsService;
    }

    @PostMapping
    public ResponseEntity sendFriendRequest(@RequestParam long id , Principal principal) {
        return friendsRequestService.addUserToFriendsWaitingList(principal , id);
    }

    @DeleteMapping
    public ResponseEntity deleteFriendRequest(@RequestParam long id , Principal principal) {
        return friendsRequestService.deleteRequest(principal , id);
    }

    @GetMapping("/requests")
    public ResponseEntity <List <UserFriendRequestDTO>> getAllUserFriendRequests(Principal principal) {
        return friendsRequestService.findRequestsByPrincipal(principal);
    }

    @GetMapping("/requests/sent")
    public ResponseEntity <List <UserFriendRequestDTO>> getUserRequestSent(Principal principal) {
        return friendsRequestService.getUserRequestSent(principal);
    }

    @PutMapping
    public ResponseEntity <List <FriendsDTO>> acceptUserToFriendsList(@RequestParam long id , Principal principal) {
        return friendsRequestService.acceptToFriendsList(principal , id);
    }

    @GetMapping
    public ResponseEntity <List <FriendsDTO>> getFriends(Principal principal) {
        return friendsService.getUserFriends(principal);
    }

    @GetMapping("/{id}")
    public ResponseEntity <List <FriendsDTO>> getUserFriends(@PathVariable("id") long id) {
        return friendsService.getUserFriendsById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteFromFriendList(Principal principal , @PathVariable long id) {
        return friendsService.deleteFromFriendList(principal , id);
    }

    @PostMapping("/message/{id}")
    public ResponseEntity <MessageDTO> sendMessage(Principal principal , @PathVariable(name = "id") Long id , @RequestBody MessageDTO messageDTO) {
        return friendsService.sendMessage(principal , id , messageDTO);
    }

    @GetMapping("/message/{id}")
    public ResponseEntity <List <MessageDTO>> sendMessage(Principal principal , @PathVariable(name = "id") Long id , @RequestParam Integer page) {
        return friendsService.getMessages(principal , id , page);
    }


}
