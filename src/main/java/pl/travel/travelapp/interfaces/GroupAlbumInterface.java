package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.GroupAlbumCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;

import java.security.Principal;
import java.util.List;

public interface GroupAlbumInterface {
    ResponseEntity <GroupAlbumDTO> create(Principal principal , GroupAlbumCreateDTO model , Long groupId);

    ResponseEntity <GroupAlbumDTO> update(Principal principal , GroupAlbumCreateDTO model , Long groupAlbumId);

    ResponseEntity <List <GroupAlbumHistoryDTO>> getAlbumHistoryByGroupAlbumId(Principal principal , Long groupAlbumId , Integer page);

    ResponseEntity <GroupAlbumDTO> setMainAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId);

    ResponseEntity <GroupAlbumDTO> setBackgroundAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId);

    ResponseEntity<GroupAlbumFullDTO> getGroupAlbumFullInformation(Principal principal , Long groupAlbumId);

    ResponseEntity deleteAlbum(Principal principal , Long groupAlbumId);
}
