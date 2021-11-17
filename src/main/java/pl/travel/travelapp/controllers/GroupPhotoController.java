package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.CommentGroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoAlbumEnterDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoDTO;
import pl.travel.travelapp.interfaces.GroupPhotoInterface;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/group/albums/photos")
@CrossOrigin(origins = "*")
public class GroupPhotoController {

    private final GroupPhotoInterface groupPhotoService;

    @Autowired
    public GroupPhotoController(GroupPhotoInterface groupPhotoService) {
        this.groupPhotoService = groupPhotoService;
    }

    @PostMapping("/multiple/{groupAlbumId}")
    public ResponseEntity <Set <GroupPhotoAlbumEnterDTO>> addNewPhotosToAlbum(Principal principal , @RequestParam("files") MultipartFile[] files , @PathVariable("groupAlbumId") Long groupAlbumId) {
        return groupPhotoService.addAlbumPhotos(principal , files , groupAlbumId);
    }

    @PostMapping("/{groupAlbumId}")
    public ResponseEntity <GroupPhotoAlbumEnterDTO> addNewPhotoToAlbum(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("groupAlbumId") Long groupAlbumId , @RequestParam("description") String description) {
        return groupPhotoService.addAlbumPhoto(principal , file , groupAlbumId , description);
    }

    @GetMapping("/{groupPhotoId}")
    public ResponseEntity <GroupPhotoDTO> getGroupPhotoByGroupPhotoId(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId) {
        return groupPhotoService.getGroupPhotoDTOByGroupPhotoId(principal , groupPhotoId);
    }

    @PutMapping("/{groupPhotoId}")
    public ResponseEntity <GroupPhotoDTO> updatePhotoDescription(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId , @RequestParam("description") String description) {
        return groupPhotoService.updatePhoto(principal , groupPhotoId , description);
    }

    @PutMapping("/{groupPhotoId}/tag")
    public ResponseEntity <GroupPhotoDTO> tagUser(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId , @RequestBody Set <Long> usersToTag) {
        return groupPhotoService.tagUsers(principal , groupPhotoId , usersToTag);
    }

    @PutMapping("/{groupPhotoId}/untag")
    public ResponseEntity <GroupPhotoDTO> untagUser(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId , @RequestBody Set <Long> usersToUntag) {
        return groupPhotoService.unTagUsers(principal , groupPhotoId , usersToUntag);
    }

    @PostMapping("/{groupPhotoId}/comments")
    public ResponseEntity <GroupCommentsDTO> addComments(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId , @RequestBody CommentGroupCreateDTO commentGroupCreateDTO) {
        return groupPhotoService.addComments(principal , groupPhotoId , commentGroupCreateDTO);
    }

    @DeleteMapping("/{groupPhotoId}")
    public ResponseEntity deletePhoto(Principal principal , @PathVariable("groupPhotoId") Long groupPhotoId) {
        return groupPhotoService.deletePhoto(principal , groupPhotoId);
    }

    @DeleteMapping("/photos")
    public ResponseEntity deletePhoto(Principal principal , @RequestBody Set <Long> photosIds) {
        return groupPhotoService.deletePhotos(principal , photosIds);
    }

}
