package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.interfaces.PhotoInterface;
import pl.travel.travelapp.models.IndividualAlbum;

import java.security.Principal;

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
        return photoService.addNewPhotoToAlbum(principal , file , id, description);
    }
}
