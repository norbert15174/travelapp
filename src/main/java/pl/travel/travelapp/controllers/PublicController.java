package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;

import java.util.List;

@Controller
@CrossOrigin(value = "*")
@RequestMapping("/public")
public class PublicController {

    private final IndividualAlbumInterface individualAlbumService;

    @Autowired
    public PublicController(IndividualAlbumInterface individualAlbumService) {
        this.individualAlbumService = individualAlbumService;
    }

    @GetMapping
    public ResponseEntity<List <IndividualAlbumDTO>> getPublicAlbums(){
        return individualAlbumService.getPublicAlbumsMainWeb();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getPublicAlbum(@PathVariable(name = "id") Long id){
        return individualAlbumService.getPublicAlbumById(id);
    }
}
