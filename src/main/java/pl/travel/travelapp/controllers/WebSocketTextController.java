package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.travel.travelapp.DTO.TextMessageDTO;
import pl.travel.travelapp.entites.ChatMessage;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebSocketTextController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketTextController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/{id}")
    public ChatMessage get(@Payload ChatMessage chatMessage, @DestinationVariable Long id) {
        return chatMessage;
    }
}
