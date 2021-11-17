package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface GroupPhotoInterface {
    ResponseEntity <Set <GroupPhotoAlbumEnterDTO>> addAlbumPhotos(Principal principal , MultipartFile[] files , Long groupAlbumId);

    ResponseEntity <GroupPhotoAlbumEnterDTO> addAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId , String description);

    ResponseEntity <List <GroupPhotoDTO>> getGroupPhotoByGroupAlbumId(Principal principal , Long groupAlbumId , Integer page);

    ResponseEntity<GroupPhotoDTO> getGroupPhotoDTOByGroupPhotoId(Principal principal , Long photoId);

    ResponseEntity <GroupPhotoDTO> updatePhoto(Principal principal , Long photoId , String description);

    ResponseEntity <GroupPhotoDTO> tagUsers(Principal principal , Long photoId , Set <Long> usersToTag);

    ResponseEntity <GroupPhotoDTO> unTagUsers(Principal principal , Long photoId , Set <Long> usersToUntag);

    ResponseEntity <GroupCommentsDTO> addComments(Principal principal , Long photoId , CommentGroupCreateDTO commentGroupCreateDTO);

    ResponseEntity deletePhoto(Principal principal , Long groupPhotoId);

    ResponseEntity deletePhotos(Principal principal , Set <Long> groupPhotoIds);
}
