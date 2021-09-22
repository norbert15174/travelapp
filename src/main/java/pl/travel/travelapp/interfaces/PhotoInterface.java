package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.DTO.photos.PhotoDTO;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.TaggedUser;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface PhotoInterface {
    List <AlbumPhotos> findUserPhotos(long albumId , long page);

    ResponseEntity <List <AlbumPhotos>> findUserPhotos(Principal principal , long albumId , long page);

    ResponseEntity <AlbumDTO> addNewPhotoToAlbum(Principal principal , MultipartFile file , long id , String description);

    ResponseEntity <AlbumDTO> addPhotosToAlbum(Principal principal , MultipartFile[] files , long id);

    ResponseEntity deleteUsersPhotos(List <Long> photoIds , Principal principal);

    ResponseEntity <List <Comments>> addCommentToPhoto(Principal principal , long photoId , Comments comment);

    ResponseEntity addTaggedUsersToPhoto(Set <Long> ids , Long photoId , Principal principal);

    ResponseEntity <PhotoDTO> modifyPhotoDescription(Long id , Principal principal , PhotoDTO photoDTO);

    ResponseEntity <List <Comments>> findCommentsByPhotoId(Long id , Principal principal);

    ResponseEntity <List <TaggedUser>> findTaggedUsersByPhotoId(Long id , Principal principal);
}
