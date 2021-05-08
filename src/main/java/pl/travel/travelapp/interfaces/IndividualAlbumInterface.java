package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;

import java.security.Principal;
import java.util.List;

public interface IndividualAlbumInterface {
    ResponseEntity<IndividualAlbumDTO> addNewAlbum(Principal principal, IndividualAlbumDTO individualAlbumDTO);
    ResponseEntity deleteAlbum(Principal principal, long id);
    ResponseEntity<IndividualAlbumDTO> findAlbum(Principal principal, long id);
    ResponseEntity<List<IndividualAlbumDTO>> findAllUserAlbums(Principal principal);
    ResponseEntity<List<IndividualAlbumDTO>> findAlbumsByUser(long id);
    ResponseEntity<IndividualAlbumDTO> findAlbumByName(String name);
    ResponseEntity<List<IndividualAlbumDTO>> findAlbumsByUserName(long id);
    ResponseEntity<IndividualAlbumDTO> modifyAlbum(Principal principal, long id);

}
