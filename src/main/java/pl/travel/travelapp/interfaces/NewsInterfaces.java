package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.NotificationDTO;

import java.security.Principal;
import java.util.List;

public interface NewsInterfaces {

    ResponseEntity <List <IndividualAlbumDTO>> getActualNews(Integer page , Principal principal);

    ResponseEntity <List <NotificationDTO>> getUserNotification(Principal principal);
}
