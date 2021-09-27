package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.NotificationDTO;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;

import java.security.Principal;
import java.util.List;

public interface NewsInterfaces {

    ResponseEntity <IndividualAlbumDTO> getActualNews(Integer page , Principal principal);

    ResponseEntity<List<NotificationDTO>> getUserNotification(Principal principal);
}
