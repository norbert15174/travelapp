package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.models.IndividualAlbum;

import java.security.Principal;
import java.util.List;

public interface IndividualAlbumInterface {
    ResponseEntity<IndividualAlbumDTO> addNewAlbum(Principal principal, IndividualAlbumDTO individualAlbumDTO);
    ResponseEntity deleteAlbum(Principal principal, long id);
    ResponseEntity<IndividualAlbum> findAlbum(Principal principal , long id);
    ResponseEntity<List<IndividualAlbumDTO>> findAllUserAlbums(Principal principal);
    ResponseEntity<List<IndividualAlbumDTO>> findAlbumsByUser(long id);
    ResponseEntity<List<IndividualAlbumDTO>> findAlbumsByName(String name, int page);
    ResponseEntity<List<IndividualAlbumDTO>> findAlbumsByUserId(long id);
    ResponseEntity<IndividualAlbumDTO> modifyAlbum(Principal principal, long id);

}
