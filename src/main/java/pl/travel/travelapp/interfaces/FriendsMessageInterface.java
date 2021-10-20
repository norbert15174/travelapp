package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.MessageDTO;

import java.security.Principal;
import java.util.List;

public interface FriendsMessageInterface {

    ResponseEntity <List <MessageDTO>> sendMessage(Principal principal , long id , MessageDTO messageDTO);

    ResponseEntity deleteMessage(Principal principal , long messageId);

    ResponseEntity <List <MessageDTO>> getMessageAfter(Principal principal , Long id , MessageDTO messageDTO);

    ResponseEntity <List <MessageDTO>> getMessages(Principal principal , Long id , Integer page);
}
