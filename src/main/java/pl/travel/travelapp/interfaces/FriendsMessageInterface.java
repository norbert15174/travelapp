package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.MessageDTO;

import java.security.Principal;

public interface FriendsMessageInterface {

    ResponseEntity<MessageDTO> sendMessage(Principal principal, long id, String message);
    ResponseEntity deleteMessage(Principal principal,long messageId);


}
