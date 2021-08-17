package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.interfaces.PhotoInterface;
import pl.travel.travelapp.models.Comments;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity addCommentToPhoto(Principal principal , @RequestBody Comments comments , @PathVariable(name = "id") Long id) {
        return photoService.addCommentToPhoto(principal , id , comments);
    }
}
