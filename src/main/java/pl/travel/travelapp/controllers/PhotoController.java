package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.DTO.photos.PhotoDTO;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.TaggedUser;
import pl.travel.travelapp.interfaces.PhotoInterface;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@CrossOrigin(value = "*")
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoInterface photoService;

    @Autowired
    public PhotoController(PhotoInterface photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/{id}")
    public ResponseEntity <AlbumDTO> addNewPhotoToAlbum(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("id") long id , @RequestParam("description") String description) {
        return photoService.addNewPhotoToAlbum(principal , file , id , description);
    }

    @PostMapping("/multiple/{id}")
    public ResponseEntity <AlbumDTO> addNewPhotosToAlbum(Principal principal , @RequestParam("files") MultipartFile[] files , @PathVariable("id") long id) {
        return photoService.addPhotosToAlbum(principal , files , id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deletePhotos(Principal principal , @RequestBody List <Long> photosIds) {
        return photoService.deleteUsersPhotos(photosIds , principal);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity <List <Comments>> addCommentToPhoto(Principal principal , @RequestBody Comments comments , @PathVariable(name = "id") Long id) {
        return photoService.addCommentToPhoto(principal , id , comments);
    }

    @PutMapping("/add/tagged/{id}")
    public ResponseEntity <Set <TaggedUser>> addTaggedUsersToPhoto(@RequestBody Set <Long> ids , @PathVariable("id") Long photoId , Principal principal) {
        return photoService.addTaggedUsersToPhoto(ids , photoId , principal);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity <PhotoDTO> modifyPhotoDescription(@PathVariable(name = "id") Long id , Principal principal , @RequestBody PhotoDTO photoDTO) {
        return photoService.modifyPhotoDescription(id , principal , photoDTO);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity <List <Comments>> getComments(@PathVariable(name = "id") Long id , Principal principal) {
        return photoService.findCommentsByPhotoId(id , principal);
    }

    @GetMapping("/tagged/{id}")
    public ResponseEntity <List <TaggedUser>> getTaggedUsers(@PathVariable(name = "id") Long id , Principal principal) {
        return photoService.findTaggedUsersByPhotoId(id , principal);
    }

    @DeleteMapping("/tagged/{id}")
    public ResponseEntity <Set <TaggedUser>> getTaggedUsers(@PathVariable(name = "id") Long id , @RequestBody Set <Long> ids , Principal principal) {
        return photoService.deleteTaggedUsersToPhoto(ids , id , principal);
    }

    @GetMapping("/{id}")
    public ResponseEntity <PhotoDTO> getPhoto(Principal principal, @PathVariable(name = "id") Long id) {
        return photoService.getPhoto(principal , id);
    }
}
