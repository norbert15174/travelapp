package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.groups.GroupAlbumCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;

import java.security.Principal;
import java.util.List;

public interface GroupAlbumInterface {
    ResponseEntity <GroupAlbumDTO> create(Principal principal , GroupAlbumCreateDTO model , Long groupId);

    ResponseEntity <List <GroupAlbumHistoryDTO>> getAlbumHistoryByGroupAlbumId(Principal principal , Long groupAlbumId , Integer page);
}
