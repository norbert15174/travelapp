package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.services.IndividualAlbumService;

import java.security.Principal;

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
}
