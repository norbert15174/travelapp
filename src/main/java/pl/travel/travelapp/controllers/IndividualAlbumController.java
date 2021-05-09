package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.services.IndividualAlbumService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class IndividualAlbumController {

    private IndividualAlbumService individualAlbumService;

    public IndividualAlbumController(IndividualAlbumService individualAlbumService) {
        this.individualAlbumService = individualAlbumService;
    }

    @PostMapping("/add")
    public ResponseEntity<IndividualAlbumDTO> addNewAlbum(@RequestBody IndividualAlbumDTO album, Principal principal){
        return individualAlbumService.addNewAlbum(principal,album);
    }
    @GetMapping
    public ResponseEntity<List <IndividualAlbumDTO>> findUserAlbums(Principal principal){
        return individualAlbumService.findAllUserAlbums(principal);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List <IndividualAlbumDTO>> findUserAlbums(@PathVariable("id") long id){
        return individualAlbumService.findAlbumsByUserId(id);
    }
    @GetMapping("/name")
    public ResponseEntity<List <IndividualAlbumDTO>> findAlbumByName(@RequestParam String album, @RequestParam(defaultValue = "0") int page){
        return individualAlbumService.findAlbumsByName(album,page);
    }
}
