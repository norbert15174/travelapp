package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.models.AlbumPhotos;

import java.security.Principal;
import java.util.List;

public interface PhotoInterface {
    List <AlbumPhotos> findUserPhotos(long albumId,long page);
    ResponseEntity<List <AlbumPhotos>> findUserPhotos(Principal principal,long albumId, long page);
    ResponseEntity <AlbumDTO> addNewPhotoToAlbum(Principal principal, MultipartFile file, long id, String description);
    ResponseEntity <AlbumDTO> addPhotosToAlbum(Principal principal, MultipartFile[] files, long id);
}
